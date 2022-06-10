package com.vladv.questsched.tabs.fragments.social.subfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.social.SocialNavFragment
import com.vladv.questsched.utilities.UserSocialProfile
import de.hdodenhof.circleimageview.CircleImageView


class SocialSearchFragment : Fragment() {

    private var _binding: FragmentSocialSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var userQuery:Query
    private lateinit var findUsersRecyclerList:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialSearchBinding.inflate(inflater, container, false)
        activity?.title = "Find Friends"
        SocialNavFragment.currentFragment = SocialSearchFragment()

        binding.socialFindUserButton.setOnClickListener {
            val searchText = binding.socialSearchUserText.text.toString()
            firebaseSearch(searchText)
        }



        return binding.root
    }

    override fun onStart() {
        super.onStart()

        firebaseSearch("")


    }


    private fun firebaseSearch(search:String)
    {
        userQuery = FirebaseDatabase.getInstance().reference.child("User").limitToLast(30)
            .orderByChild("username").startAt(search).endAt(search + "\uf8ff")


        findUsersRecyclerList = binding.allUsersList
        findUsersRecyclerList.layoutManager =  LinearLayoutManager(context)

        val options = FirebaseRecyclerOptions.Builder<UserSocialProfile>()
            .setQuery(userQuery, UserSocialProfile::class.java)
            .build()

        val adapter : FirebaseRecyclerAdapter<UserSocialProfile, FindUsersViewHolder> =
            object : FirebaseRecyclerAdapter<UserSocialProfile, FindUsersViewHolder>(options){
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): FindUsersViewHolder {

                    val binding = FragmentSocialSearchItemBinding.inflate(layoutInflater)
                    val view: View = binding.root
                    return FindUsersViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: FindUsersViewHolder,
                    position: Int,
                    model: UserSocialProfile
                ) {
                    holder.username?.text = model.username
                    holder.description?.text = model.profileDescription
                    model.avatar?.drawableFace?.let { holder.image?.setImageResource(it) }
                    holder.viewButton?.setOnClickListener {
                        val visitUserId = getRef(position).key
                        activity?.supportFragmentManager?.commit {
                            setCustomAnimations(
                                R.anim.fragment_fadein,
                                R.anim.fragment_fadeout,
                                R.anim.fragment_fadein,
                                R.anim.fragment_fadeout
                            )
                            replace(
                                R.id.flFragment,
                                SocialUserProfile(visitUserId!!)
                            )
                        }
                    }
                }

            }
        findUsersRecyclerList.adapter = adapter

        adapter.startListening()
    }



    companion object{
        class FindUsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var username:TextView? =null
            var description:TextView? =null
            var image:CircleImageView?=null
            var viewButton: ImageButton?=null

            init{
                username = itemView.findViewById(R.id.userSocialName)
                description = itemView.findViewById(R.id.userSocialDescription)
                image = itemView.findViewById(R.id.userSocialImage)
                viewButton = itemView.findViewById(R.id.searchViewProfileButton)
            }
        }

    }


}