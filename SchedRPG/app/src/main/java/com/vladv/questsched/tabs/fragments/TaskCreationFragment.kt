package com.vladv.questsched.tabs.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentTaskCreationBinding
import com.vladv.questsched.myfirebasetool.ChangeFirebaseData
import com.vladv.questsched.tabs.TasksViewTab
import com.vladv.questsched.user.User
import com.vladv.questsched.user.UserTask


@Suppress("DEPRECATION")
class TaskCreationFragment : Fragment() {

    private var _binding: FragmentTaskCreationBinding? = null
    private val binding get() = _binding!!
    private var user = User()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskCreationBinding.inflate(inflater, container, false)

//        val stats = arrayOf(
//            "Strength",
//            "Dexterity",
//            "Constitution",
//            "Wisdom",
//            "Intelligence",
//            "Charisma"
//        )
//        val adapter = ArrayAdapter(requireView().context, android.R.layout.simple_spinner_dropdown_item, stats)
//        binding.createTaskType.adapter = adapter
//        val difficulty = arrayOf("very easy", "easy", "medium", "hard", "very hard")
//        val adapter2 = ArrayAdapter(requireView().context, android.R.layout.simple_spinner_dropdown_item, difficulty)
//        binding.createTaskDifficulty.adapter = adapter2

        binding.createTaskSubmit.setOnClickListener { addTaskToFirebase() }

        binding.createTaskBack.setOnClickListener {
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.flFragment, HomeFragment());
            transaction.commit()
        }



        return binding.root
    }


    private fun addTaskToFirebase() {
        val name = binding.createTaskName.text.toString().trim { it <= ' ' }
        val description = binding.createTaskDescription.text.toString().trim { it <= ' ' }
        val difficulty = binding.createTaskDifficulty.selectedItemPosition
        val type = binding.createTaskType.selectedItemPosition
        if (name.isEmpty()) {
            binding.createTaskName.error = "Name is required."
            binding.createTaskName.requestFocus()
            return
        }
        val userTask = UserTask(type, difficulty, name, description)
        user.addTask(userTask)
        val changeFirebaseData = ChangeFirebaseData()
        changeFirebaseData.updateUserData(user)
        makeToast("Task: " + userTask.name + " created succesfully")
        refreshFragment()
    }

    private fun makeToast(s: String) {
        Toast.makeText(TasksViewTab.context, s, Toast.LENGTH_SHORT).show()
    }

    private fun refreshFragment()
    {
        val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
        }
        ft.detach(this).attach(this).commit()
    }

}