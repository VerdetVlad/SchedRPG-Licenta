package com.vladv.questsched.tabs

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schedrpg.databinding.ActivityTaskCreationTabBinding
import com.vladv.questsched.myfirebasetool.ChangeFirebaseData
import com.vladv.questsched.user.User
import com.vladv.questsched.user.UserTask

class TaskCreationTab : AppCompatActivity() {
    private var binding: ActivityTaskCreationTabBinding? = null
    private var user = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskCreationTabBinding.inflate(
            layoutInflater
        )
        val view: View = binding!!.root
        setContentView(view)
        val stats = arrayOf(
            "Strength",
            "Dexterity",
            "Constitution",
            "Wisdom",
            "Intelligence",
            "Charisma"
        )
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, stats)
        binding!!.createTaskType.adapter = adapter
        val difficulty = arrayOf("very easy", "easy", "medium", "hard", "very hard")
        val adapter2 = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, difficulty)
        binding!!.createTaskDifficulty.adapter = adapter2
        binding!!.createTaskSubmit.setOnClickListener { addTaskToFirebase() }
        binding!!.createTaskBack.setOnClickListener {
            startActivity(
                Intent(
                    this@TaskCreationTab,
                    LogInSuccess::class.java
                )
            )
        }
    }

    private fun addTaskToFirebase() {
        val name = binding!!.createTaskName.text.toString().trim { it <= ' ' }
        val description = binding!!.createTaskDescription.text.toString().trim { it <= ' ' }
        val difficulty = binding!!.createTaskDifficulty.selectedItemPosition
        val type = binding!!.createTaskType.selectedItemPosition
        if (name.isEmpty()) {
            binding!!.createTaskName.error = "Name is required."
            binding!!.createTaskName.requestFocus()
            return
        }
        val userTask = UserTask(type, difficulty, name, description)
        user.addTask(userTask)
        val changeFirebaseData = ChangeFirebaseData()
        changeFirebaseData.updateUserData(user)
        makeToast("Task: " + userTask.name + " created succesfully")
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun makeToast(s: String) {
        Toast.makeText(TasksViewTab.context, s, Toast.LENGTH_SHORT).show()
    }
}