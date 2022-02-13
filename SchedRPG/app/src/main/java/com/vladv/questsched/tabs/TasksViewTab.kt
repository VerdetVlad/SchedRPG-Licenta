package com.vladv.questsched.tabs


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ListView
import com.vladv.questsched.user.UserTask
import com.vladv.questsched.myfirebasetool.ChangeFirebaseData
import android.widget.Toast
import com.example.schedrpg.databinding.ActivityTasksViewTabBinding
import com.vladv.questsched.user.User

class TasksViewTab : AppCompatActivity() {
    private var binding: ActivityTasksViewTabBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksViewTabBinding.inflate(
            layoutInflater
        )
        val view: View = binding!!.root
        setContentView(view)
        context = applicationContext
        val user = User()
        listAdapter = TaskListAdapter(this@TasksViewTab, user.tasks)
        listView = binding!!.listview
        listView!!.adapter = listAdapter
    }

    companion object {
        private var user = User()

        @SuppressLint("StaticFieldLeak")
        var listView: ListView? = null

        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var listAdapter: TaskListAdapter? = null
        fun removeItem(task: UserTask) {
            makeToast("Removed task: " + task.name)
            user.removeTask(task)
            val changeFirebaseData = ChangeFirebaseData()
            changeFirebaseData.updateUserData(user)
            listView!!.adapter = listAdapter
        }

        private fun makeToast(s: String) {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
        }
    }
}