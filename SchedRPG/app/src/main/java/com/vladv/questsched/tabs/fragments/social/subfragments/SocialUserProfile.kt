package com.vladv.questsched.tabs.fragments.social.subfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentSocialUserProfileBinding
import com.example.schedrpg.databinding.FragmentSocialUserProfileItemBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.FirebaseData
import com.vladv.questsched.utilities.SentFriendRequest
import com.vladv.questsched.utilities.UserSocialProfile


class SocialUserProfile : Fragment {

    private lateinit var viewedUserID: String
    private val currentUserId = MyFragmentManager.firebaseAuth.uid!!


    constructor()
    constructor(viewedUserID: String) : super() {
        this.viewedUserID = viewedUserID

    }

    private var _binding: FragmentSocialUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userRef: DatabaseReference
    private lateinit var friendReqRef: DatabaseReference
    private lateinit var friendListRef: DatabaseReference
    private var friendshipState:String = "not friends"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSocialUserProfileBinding.inflate(inflater, container, false)

        userRef = FirebaseDatabase.getInstance().reference.child("User")
        friendReqRef = FirebaseDatabase.getInstance().reference.child("FriendRequest")
        friendListRef = FirebaseDatabase.getInstance().reference.child("FriendList")

        binding.sendFriendRequestButton.setOnClickListener{
            sendFriendRequest()
        }

        binding.cancelFriendRequesButton.setOnClickListener{
            cancelFriendRequest()
        }

        binding.removeFriendButton.setOnClickListener{
            removeFriend()
        }

        binding.requestReceivedButton.setOnClickListener{
            acceptRequest()
        }


        MyFragmentManager.currentFragment = this


        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if(!this::viewedUserID.isInitialized) return

        friendReqRef.child(currentUserId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChild(viewedUserID)) {

                        val requestType = dataSnapshot.child(viewedUserID)
                            .child("type").value.toString()

                        if (requestType == "sent") {
                            friendshipState = "request sent"

                        }
                        else if (requestType == "received")
                        {
                            friendshipState = "request received"
                        }
                        manageRequests()

                    }
                    else
                    {
                        if(friendshipState == "friends")
                        {
                            friendshipState = "not friends"
                        }
                        else{
                            friendListRef.child(currentUserId)
                                .addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.hasChild(viewedUserID))
                                        {
                                            friendshipState = "friends"
                                            manageRequests()
                                            showQuests()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }

                                })
                        }

                    }

                    manageRequests()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })


        setData()
    }




    private fun showQuests() {
        val questQuery = FirebaseDatabase.getInstance().reference.child("User").child(viewedUserID).child("quests")

        val findQuestRecycleList = binding.socialUserProfileQuestList
        findQuestRecycleList.layoutManager =  LinearLayoutManager(context)

        val options = FirebaseRecyclerOptions.Builder<Quest>()
            .setQuery(questQuery, Quest::class.java)
            .build()

        val adapter : FirebaseRecyclerAdapter<Quest, FindUserQuestsViewHolder> =
            object : FirebaseRecyclerAdapter<Quest, FindUserQuestsViewHolder>(options){
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): FindUserQuestsViewHolder {

                    val binding = FragmentSocialUserProfileItemBinding.inflate(layoutInflater)
                    val view: View = binding.root
                    return FindUserQuestsViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: FindUserQuestsViewHolder,
                    position: Int,
                    model: Quest
                ) {
                    holder.questName?.text = model.name
                    holder.questDifficulty?.text = model.difficultyStringValue()
                    holder.questType?.text = model.typeStringValue()
                    model.typeImageValue().let { holder.questTypeImage?.setImageResource(it) }

                    holder.questDescription?.text = if(model.description == "")  "No description" else model.description

                    holder.questLayout?.setOnClickListener{

                        context?.let { MyFragmentManager.createQuestPopUp(model, it) }
                    }

                    if(currentUserId == viewedUserID)
                    {
                        holder.copyButton?.visibility =View.GONE
                    }


                    holder.copyButton?.setOnClickListener {
                        if (User().quests?.contains(model) == true) Toast.makeText(context,"Quest already owned",Toast.LENGTH_SHORT).show()
                        else
                        {
                            User().quests?.add(model)
                            FirebaseData().updateUserData(activity!!,"Quest copied","Failed to add to database")
                        }
                    }
                }

            }
        findQuestRecycleList.adapter = adapter

        adapter.startListening()
    }

    companion object{
        class FindUserQuestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var questName: TextView? =null
            var questType: TextView? =null
            var questTypeImage: ImageView? =null
            var questDifficulty: TextView? =null
            var questDescription: TextView? =null
            var copyButton: ImageButton?=null
            var questLayout: LinearLayout?=null

            init{
                questName = itemView.findViewById(R.id.socialQuestName)
                questType = itemView.findViewById(R.id.socialQuestType)
                questTypeImage = itemView.findViewById(R.id.socialQuestTypeImage)
                questDifficulty = itemView.findViewById(R.id.socialQuestDificulty)
                questDescription = itemView.findViewById(R.id.socialQuestDescription)
                copyButton = itemView.findViewById(R.id.copyFriendQuest)
                questLayout = itemView.findViewById(R.id.friendProfileQuestLayout)
            }
        }

    }


    private fun setData() {
        userRef.child(viewedUserID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val profile = dataSnapshot.getValue(UserSocialProfile::class.java)

                if (profile != null) {
                    profile.avatar?.drawableFace?.let { binding.visitProfileImage.setImageResource(it) }
                    binding.visitUserName.text = profile.username
                    binding.userProfileDescription.text = profile.profileDescription

                }

                manageRequests()

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    private fun manageRequests() {


        if(currentUserId == viewedUserID)
        {
            binding.requestConstrLayout.visibility = View.GONE
            showQuests()
            return
        }
        when (friendshipState) {
            "request sent" -> {
                binding.sendFriendRequestButton.visibility = View.GONE
                binding.cancelFriendRequesButton.visibility = View.VISIBLE
                binding.removeFriendButton.visibility = View.GONE
                binding.requestReceivedButton.visibility = View.GONE
                binding.socialUserProfileQuestList.visibility = View.GONE
            }
            "not friends" -> {
                binding.sendFriendRequestButton.visibility = View.VISIBLE
                binding.cancelFriendRequesButton.visibility = View.GONE
                binding.removeFriendButton.visibility = View.GONE
                binding.requestReceivedButton.visibility = View.GONE
                binding.socialUserProfileQuestList.visibility = View.GONE
            }
            "friends" -> {
                binding.sendFriendRequestButton.visibility = View.GONE
                binding.cancelFriendRequesButton.visibility = View.GONE
                binding.removeFriendButton.visibility = View.VISIBLE
                binding.requestReceivedButton.visibility = View.GONE
                binding.socialUserProfileQuestList.visibility = View.VISIBLE
            }
            "request received" -> {
                binding.sendFriendRequestButton.visibility = View.GONE
                binding.cancelFriendRequesButton.visibility = View.GONE
                binding.removeFriendButton.visibility = View.GONE
                binding.requestReceivedButton.visibility = View.VISIBLE
                binding.socialUserProfileQuestList.visibility = View.GONE
            }
        }
    }

    private fun sendFriendRequest() {
        friendReqRef.child(currentUserId).child(viewedUserID)
            .child("type").setValue("sent")
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    friendReqRef.child(viewedUserID).child(currentUserId)
                        .setValue(SentFriendRequest())
                        .addOnCompleteListener{
                            friendshipState = "request sent"
                            manageRequests()
                        }
                    friendReqRef.child(viewedUserID).child(currentUserId)

                }
            }


    }

    private fun cancelFriendRequest() {
        friendReqRef.child(currentUserId).child(viewedUserID)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    friendReqRef.child(viewedUserID).child(currentUserId)
                        .removeValue()
                        .addOnCompleteListener{
                            if (it.isSuccessful) {
                                friendshipState = "not friends"
                                manageRequests()
                            }
                        }
                }
            }
    }

    private fun removeFriend() {
        friendListRef.child(currentUserId).child(viewedUserID)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    friendListRef.child(viewedUserID).child(currentUserId)
                        .removeValue()
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                friendshipState = "not friends"
                                manageRequests()
                            }
                        }
                }
            }

    }

    private fun acceptRequest() {
        friendListRef.child(currentUserId).child(viewedUserID)
            .child("Friends").setValue("Saved")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    friendListRef.child(viewedUserID).child(currentUserId)
                        .child("Friends").setValue("Saved")
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                friendReqRef.child(currentUserId).child(viewedUserID)
                                    .removeValue()
                                    .addOnCompleteListener { task3 ->
                                        if (task3.isSuccessful) {
                                            friendReqRef.child(viewedUserID).child(currentUserId)
                                                .removeValue()
                                                .addOnCompleteListener {
                                                    friendshipState = "friends"
                                                    manageRequests()
                                                }
                                        }
                                    }
                            }
                        }
                }
            }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(!this::viewedUserID.isInitialized) return
        outState.putString("friendID", viewedUserID)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            viewedUserID = savedInstanceState.getString("friendID")!!

        }
    }


}