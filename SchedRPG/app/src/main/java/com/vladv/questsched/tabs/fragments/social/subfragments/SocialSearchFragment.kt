package com.vladv.questsched.tabs.fragments.social.subfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentSocialSearchBinding
import com.example.schedrpg.databinding.FragmentSocialSearchItemBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.home.subfragments.HomeQuestsFragment
import com.vladv.questsched.tabs.fragments.questlistview.EditQuestFragment
import com.vladv.questsched.tabs.fragments.social.SocialNavFragment
import com.vladv.questsched.tabs.fragments.social.UserSocial
import de.hdodenhof.circleimageview.CircleImageView


class SocialSearchFragment : Fragment() {

    private var _binding: FragmentSocialSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRef:DatabaseReference
    private lateinit var FindFriendsRecyclerList:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialSearchBinding.inflate(inflater, container, false)
        activity?.title = "Find friends"
        SocialNavFragment.currentFragment = SocialSearchFragment()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        userRef = FirebaseDatabase.getInstance().reference.child("User")

        FindFriendsRecyclerList = binding.allUsersList
        FindFriendsRecyclerList.layoutManager =  LinearLayoutManager(context)

        val options = FirebaseRecyclerOptions.Builder<UserSocial>()
            .setQuery(userRef, UserSocial::class.java)
            .build()

        val adapter : FirebaseRecyclerAdapter<UserSocial, FindFriendViewHolder> =
            object : FirebaseRecyclerAdapter<UserSocial, FindFriendViewHolder>(options){
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): FindFriendViewHolder {

                    val binding = FragmentSocialSearchItemBinding.inflate(layoutInflater)
                    val view: View = binding.root
                    return FindFriendViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: FindFriendViewHolder,
                    position: Int,
                    model: UserSocial
                ) {
                    holder.username?.text = model.username
                    model.avatar?.drawableFace?.let { holder.image?.setImageResource(it) }
                    holder.addButton?.setOnClickListener {
                        val visitUserId = getRef(position).key
                        activity?.supportFragmentManager?.commit {
                            setCustomAnimations(
                                R.anim.fragment_fadein,
                                R.anim.fragment_fadeout,
                                R.anim.fragment_fadein,
                                R.anim.fragment_fadeout
                            )
                            replace(
                                MyFragmentManager.binding.flFragment.id,
                                SocialUserProfile(visitUserId!!)
                            )
                            addToBackStack(null)
                        }
                    }
                }

            }
        FindFriendsRecyclerList.adapter = adapter

        adapter.startListening()
    }

    companion object{



        class FindFriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var username:TextView? =null
            var image:CircleImageView?=null
            var addButton: Button?=null

            init{
                username = itemView.findViewById(R.id.userSocialName)
                image = itemView.findViewById(R.id.userSocialImage)
                addButton = itemView.findViewById(R.id.friendReqButton)
            }
        }

    }


}