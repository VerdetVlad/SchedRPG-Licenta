package com.vladv.questsched.tabs.fragments.social.subfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentSocialRequestBinding
import com.example.schedrpg.databinding.FragmentSocialRequestItemBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.social.SocialNavFragment
import com.vladv.questsched.utilities.SentFriendRequest
import de.hdodenhof.circleimageview.CircleImageView


class SocialRequestFragment : Fragment() {

    private var _binding: FragmentSocialRequestBinding? = null
    private val binding get() = _binding!!
    private lateinit var userQuery: Query
    private lateinit var friendRequestRecyclerList: RecyclerView
    private lateinit var friendReqRef: DatabaseReference
    private lateinit var friendListRef: DatabaseReference
    private val currentUserId = MyFragmentManager.firebaseAuth.uid!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSocialRequestBinding.inflate(inflater, container, false)
        activity?.title = "Friend Request"
        SocialNavFragment.currentFragment = SocialFriendsFragment()



        return binding.root
    }

    override fun onStart() {
        super.onStart()

        friendListRef = FirebaseDatabase.getInstance().reference.child("FriendList")
        friendReqRef = FirebaseDatabase.getInstance().reference.child("FriendRequest")
        userQuery = friendReqRef.child(currentUserId)
            .orderByChild("type")
            .equalTo("received")
            .limitToLast(30)


        friendRequestRecyclerList = binding.friendRequestList
        friendRequestRecyclerList.layoutManager =  LinearLayoutManager(context)

        val options = FirebaseRecyclerOptions.Builder<SentFriendRequest>()
            .setQuery(userQuery, SentFriendRequest::class.java)
            .build()

        val adapter : FirebaseRecyclerAdapter<SentFriendRequest, FindRequestViewHolder> =
            object : FirebaseRecyclerAdapter<SentFriendRequest, FindRequestViewHolder>(options){
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): FindRequestViewHolder {

                    val binding = FragmentSocialRequestItemBinding.inflate(layoutInflater)
                    val view: View = binding.root
                    return FindRequestViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: FindRequestViewHolder,
                    position: Int,
                    model: SentFriendRequest
                ) {
                    holder.username?.text = model.username
                    model.avatarFace?.let { holder.image?.setImageResource(it) }

                    val senderUserId = getRef(position).key
                    holder.acceptButton?.setOnClickListener {
                        acceptRequest(senderUserId!!)
                    }
                    holder.declineButton?.setOnClickListener{
                        declineRequest(senderUserId!!)
                    }
                }

            }

        friendRequestRecyclerList.adapter = adapter

        adapter.startListening()
    }



    private fun acceptRequest(senderUserID : String) {
        friendListRef.child(senderUserID).child(currentUserId)
            .child("Friends").setValue("Saved")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    friendListRef.child(currentUserId).child(senderUserID)
                        .child("Friends").setValue("Saved")
                        .addOnCompleteListener { task1 ->
                            if (task1.isSuccessful) {
                                friendReqRef.child(senderUserID).child(currentUserId)
                                    .removeValue()
                                    .addOnCompleteListener { task2 ->
                                        if (task2.isSuccessful) {
                                            friendReqRef.child(currentUserId).child(senderUserID)
                                                .removeValue()
                                                .addOnCompleteListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Request accepted",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        }
                                    }
                            }
                        }
                }
            }
    }

    private fun declineRequest(senderUserID : String) {
        friendReqRef.child(currentUserId).child(senderUserID)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    friendReqRef.child(senderUserID).child(currentUserId)
                        .removeValue()
                        .addOnCompleteListener{
                            if (it.isSuccessful) {
                                Toast.makeText(context,"Request declined",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }

    companion object{

        class FindRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var username: TextView? =null
            var image: CircleImageView?=null
            var acceptButton: ImageButton?=null
            var declineButton: ImageButton?=null

            init{
                username = itemView.findViewById(R.id.userSocialName)
                image = itemView.findViewById(R.id.userSocialImage)
                acceptButton = itemView.findViewById(R.id.friendReqAcceptButton)
                declineButton = itemView.findViewById(R.id.friendReqDeclineButton)
            }
        }

    }



}