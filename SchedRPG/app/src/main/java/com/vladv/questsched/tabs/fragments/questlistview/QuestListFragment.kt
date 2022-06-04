package com.vladv.questsched.tabs.fragments.questlistview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.schedrpg.databinding.FragmentQuestViewBinding
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.social.subfragments.SocialSearchFragment
import com.vladv.questsched.utilities.FirebaseData
import com.vladv.questsched.user.User
import com.vladv.questsched.user.Quest

class QuestListFragment : Fragment() {

    private var _binding: FragmentQuestViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestViewBinding.inflate(inflater, container, false)
        activity?.title = "Quest List"
        MyFragmentManager.currentFragment = QuestListFragment()


        auxActivity = activity

        if(User().quests.isNullOrEmpty()) return binding.root
        listAdapter = QuestListAdapter(requireContext(), User().quests)
        listView = binding.listview
        listView!!.adapter = listAdapter


        return binding.root
    }




    companion object {
        private var user = User()

        @SuppressLint("StaticFieldLeak")
        var listView: ListView? = null

        var auxActivity: FragmentActivity? = null


        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var listAdapter: QuestListAdapter? = null
        fun removeItem(task: Quest) {
            user.removeQuest(task)
            MyFragmentManager.currentFragment = QuestListFragment()
            FirebaseData().updateUserData()
        }





    }
}
