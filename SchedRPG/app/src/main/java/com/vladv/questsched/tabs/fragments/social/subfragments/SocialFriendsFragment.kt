package com.vladv.questsched.tabs.fragments.social.subfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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

                    userRef.child(friendID!!).addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val friendProfile = snapshot.getValue(UserSocialProfile::class.java) ?: return

                            holder.username?.text = friendProfile.username
                            friendProfile.avatar?.drawableFace?.let { holder.image?.setImageResource(it) }
                            holder.viewButton?.setOnClickListener {
                                activity?.supportFragmentManager?.commit {
                                    setCustomAnimations(
                                        R.anim.fragment_fadein,
                                        R.anim.fragment_fadeout,
                                        R.anim.fragment_fadein,
                                        R.anim.fragment_fadeout
                                    )
                                    replace(
                                        MyFragmentManager.binding.flFragment.id,
                                        SocialUserProfile(friendID)
                                    )
                                    addToBackStack(null)
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
                                        MyFragmentManager.binding.flFragment.id,
                                        SocialChatFragment(friendID,friendProfile)
                                    )
                                    addToBackStack(null)
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


    companion object{
        class FindFriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var username: TextView? =null
            var image: CircleImageView?=null
            var viewButton: ImageButton?=null
            var messageButton: ImageButton? =null

            init{
                username = itemView.findViewById(R.id.userSocialName)
                image = itemView.findViewById(R.id.userSocialImage)
                viewButton = itemView.findViewById(R.id.friendViewProfileButton)
                messageButton = itemView.findViewById(R.id.friendMessageButton)
            }
        }

    }

}