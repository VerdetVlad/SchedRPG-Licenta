package com.vladv.questsched.tabs.fragments.social.subfragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentSocialFriendsBinding
import com.example.schedrpg.databinding.FragmentSocialFriendsItemBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.social.SocialNavFragment
import com.vladv.questsched.utilities.UserSocialProfile
import de.hdodenhof.circleimageview.CircleImageView


class SocialFriendsFragment : Fragment() {

    private var _binding: FragmentSocialFriendsBinding? = null
    private val binding get() = _binding!!

    private val currentUser = MyFragmentManager.firebaseAuth.uid
    private lateinit var friendsRef:DatabaseReference
    private lateinit var userRef:DatabaseReference
    private lateinit var lasMessageRef: DatabaseReference
    private lateinit var findFriendsRecyclerList:RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialFriendsBinding.inflate(inflater, container, false)
        activity?.title = "Friends List"
        SocialNavFragment.currentFragment = SocialFriendsFragment()



        friendsRef = FirebaseDatabase.getInstance().reference.child("FriendList").child(currentUser!!)
        userRef = FirebaseDatabase.getInstance().reference.child("User")
        lasMessageRef = FirebaseDatabase.getInstance().reference.child("Messages").child(currentUser)

        return binding.root
    }

    override fun onStart() {
        super.onStart()


        findFriendsRecyclerList = binding.socialFriendList
        findFriendsRecyclerList.layoutManager =  LinearLayoutManager(context)

        val options = FirebaseRecyclerOptions.Builder<UserSocialProfile>()
            .setQuery(friendsRef, UserSocialProfile::class.java)
            .build()

        val adapter : FirebaseRecyclerAdapter<UserSocialProfile, FindFriendViewHolder> =
            object : FirebaseRecyclerAdapter<UserSocialProfile, FindFriendViewHolder>(options){
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): FindFriendViewHolder{

                    val binding = FragmentSocialFriendsItemBinding.inflate(layoutInflater)
                    val view: View = binding.root
                    return FindFriendViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: FindFriendViewHolder,
                    position: Int,
                    model: UserSocialProfile
                ) {
                    val friendID = getRef(position).key

                    lasMessageRef.child(friendID!!).orderByKey().limitToLast(1).addChildEventListener(object :ChildEventListener{
                        override fun onChildAdded(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            handleMessageSnapshot(holder,snapshot)
                        }

                        override fun onChildChanged(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                        }

                        override fun onChildRemoved(snapshot: DataSnapshot) {
                        }

                        override fun onChildMoved(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(context,"Error retrieving messages",Toast.LENGTH_SHORT).show()
                        }

                    })


                    userRef.child(friendID).addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {

                            val username = snapshot.child("username").value.toString()
                            val image = snapshot.child("avatar").child("drawableFace").value.toString().toInt()

                            holder.username?.text = username
                            image.let { holder.image?.setImageResource(it) }
                            holder.viewButton?.setOnClickListener {
                                activity?.supportFragmentManager?.commit {
                                    setCustomAnimations(
                                        R.anim.fragment_fadein,
                                        R.anim.fragment_fadeout,
                                        R.anim.fragment_fadein,
                                        R.anim.fragment_fadeout
                                    )
                                    replace(
                                        R.id.flFragment,
                                        SocialUserProfile(friendID)
                                    )
                                }
                            }

                            holder.messageButton?.setOnClickListener {
                                activity?.supportFragmentManager?.commit {
                                    setCustomAnimations(
                                        R.anim.fragment_fadein,
                                        R.anim.fragment_fadeout,
                                        R.anim.fragment_fadein,
                                        R.anim.fragment_fadeout
                                    )
                                    replace(
                                        R.id.flFragment,
                                        SocialChatFragment(friendID,username,image)
                                    )
                                }
                            }



                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(context,"Error retrieving data",Toast.LENGTH_SHORT).show()
                        }
                    })


                }

            }
        findFriendsRecyclerList.adapter = adapter

        adapter.startListening()


    }

    private fun handleMessageSnapshot(holder: FindFriendViewHolder, snapshot: DataSnapshot)
    {
        val from = snapshot.child("from").value.toString()
        val seen = snapshot.child("seenStatus").value.toString()
        val message:String
        if(from == currentUser) {
            message = "You: " + snapshot.child("message").value.toString()
            holder.lastMessage?.typeface = Typeface.DEFAULT
        }
        else {
            if(seen == "unseen") {
                holder.lastMessage?.typeface = Typeface.DEFAULT_BOLD
                holder.lastMessage?.setTextColor(Color.parseColor("#FF5722"))
            }

            message ="Them: " + snapshot.child("message").value.toString()
        }
        holder.lastMessage?.text = message

    }


    companion object{
        class FindFriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var username: TextView? =null
            var image: CircleImageView?=null
            var viewButton: ImageButton?=null
            var messageButton: ImageButton? =null
            var lastMessage: TextView? =null

            init{
                username = itemView.findViewById(R.id.userSocialName)
                image = itemView.findViewById(R.id.userSocialImage)
                viewButton = itemView.findViewById(R.id.friendViewProfileButton)
                messageButton = itemView.findViewById(R.id.friendMessageButton)
                lastMessage = itemView.findViewById(R.id.userSocialLastMessage)
            }
        }

    }

}