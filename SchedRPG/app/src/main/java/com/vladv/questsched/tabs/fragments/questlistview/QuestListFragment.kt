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
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.FirebaseUtilities

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


        MyFragmentManager.currentFragment = this

        auxActivity = activity

        auxQuestList = User().quests
        if(auxQuestList.isNullOrEmpty()) {
            binding.noQuestTextView3.visibility = View.VISIBLE
            return binding.root
        }

        binding.noQuestTextView3.visibility = View.GONE

        listAdapter = QuestListAdapter(requireContext(), auxQuestList)
        listView = binding.listview
        listView!!.adapter = listAdapter


        return binding.root
    }




    companion object {
        private var user = User()

        @SuppressLint("StaticFieldLeak")
        var listView: ListView? = null
        var auxQuestList : ArrayList<Quest>?=null

        var auxActivity: FragmentActivity? = null


        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var listAdapter: QuestListAdapter? = null
        fun removeItem(quest: Quest) {
            user.removeQuest(quest)
            auxQuestList?.remove(quest)

            user.lastLogIn?.unfinishedQuests?.remove(quest)
            listAdapter?.notifyDataSetChanged()
            FirebaseUtilities().updateUserData(auxActivity!!,"Quest:${quest.name} removed successfully", "Quest:${quest.name} removal failed")
        }




    }






}
