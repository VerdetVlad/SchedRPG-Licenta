package com.vladv.questsched.tabs.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentTaskViewBinding
import com.vladv.questsched.myfirebasetool.FirebaseData
import com.vladv.questsched.user.User
import com.vladv.questsched.user.Quest

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskViewBinding.inflate(inflater, container, false)
        activity?.title = "Quest List"

        val user = User()
        listAdapter = TaskListAdapter(requireContext(), user.quests)
        listView = binding.listview
        listView!!.adapter = listAdapter


        return binding.root
    }




    companion object {
        private var user = User()

        @SuppressLint("StaticFieldLeak")
        var listView: ListView? = null


        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var listAdapter: TaskListAdapter? = null
        fun removeItem(task: Quest) {
            user.removeQuest(task)
            val changeFirebaseData = FirebaseData()
            changeFirebaseData.updateUserData(user)
            listView!!.adapter = listAdapter
        }

    }
}
