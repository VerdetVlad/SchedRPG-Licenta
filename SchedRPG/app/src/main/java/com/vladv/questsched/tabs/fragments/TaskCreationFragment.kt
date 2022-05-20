package com.vladv.questsched.tabs.fragments

import java.util.Vector
import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentTaskCreationBinding
import com.vladv.questsched.myfirebasetool.FirebaseData
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.RecurringQuest
import com.vladv.questsched.user.User
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class TaskCreationFragment : Fragment() {

    private var _binding: FragmentTaskCreationBinding? = null
    private val binding get() = _binding!!
    private var user = User()

    private var editTextDate: EditText? = null
    private var buttonDate: Button? = null
    private var lastSelectedYear = 0
    private var lastSelectedMonth = 0
    private var lastSelectedDayOfMonth = 0
    private  var weekDays: ArrayList<CheckBox> = ArrayList<CheckBox>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskCreationBinding.inflate(inflater, container, false)
        activity?.title = "Quest Creation"

        val stats = arrayOf("Strength","Dexterity", "Constitution", "Wisdom", "Intelligence","Charisma")
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            stats)
        binding.createTaskType.adapter = adapter

        val difficulty = arrayOf("very easy", "easy", "medium", "hard", "very hard")
        val adapter2 = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            difficulty)
        binding.createTaskDifficulty.adapter = adapter2

        val repeatOptions = arrayOf("never", "daily", "weekly", "monthly")
        val adapter3 = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            repeatOptions)
        binding.repeatSpinner.adapter = adapter3


        weekDays.addAll(listOf(
            binding.monRB,
            binding.tueRB,
            binding.wenRB,
            binding.thrRB,
            binding.frdRB,
            binding.satRB,
            binding.sunRB
        ))


        binding.repeatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val spinChoice = binding.repeatSpinner.selectedItemPosition
                if(spinChoice==2) binding.creationLinLayout.visibility = View.VISIBLE
                else {
                    binding.creationLinLayout.visibility = View.GONE
                    resetWeekFields()
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }



        editTextDate = binding.editTextDate
        buttonDate = binding.buttonDate
        buttonDate!!.setOnClickListener { buttonSelectDate() }
        val c = Calendar.getInstance()
        lastSelectedYear = c[Calendar.YEAR]

        lastSelectedMonth = c[Calendar.MONTH] + 1
        var lastSelectedMonthString = "" + lastSelectedMonth
        if(lastSelectedMonth<10) lastSelectedMonthString = "0" + lastSelectedMonthString;

        lastSelectedDayOfMonth = c[Calendar.DAY_OF_MONTH]
        var lastSelectedDayOfMonthString = "" + lastSelectedMonth
        if(lastSelectedMonth<10) lastSelectedDayOfMonthString = "0" + lastSelectedDayOfMonthString;

        editTextDate!!.setText(lastSelectedDayOfMonthString + "/" + lastSelectedMonthString + "/" + lastSelectedYear)



        binding.createTaskSubmit.setOnClickListener { addTaskToFirebase() }

        return binding.root
    }


    private fun addTaskToFirebase() {
        val name = binding.createTaskName.text.toString().trim { it <= ' ' }
        if (name.isEmpty()) {
            binding.createTaskName.error = "Name is required."
            binding.createTaskName.requestFocus()
            return
        }
        val description = binding.createTaskDescription.text.toString().trim { it <= ' ' }
        val difficulty = binding.createTaskDifficulty.selectedItemPosition
        val type = binding.createTaskType.selectedItemPosition
        val date = binding.editTextDate.text.toString()
        val repeatType = binding.createTaskDifficulty.selectedItemPosition

        val checkedWeeks = arrayListOf(
            weekDays[0].isChecked,
            weekDays[1].isChecked,
            weekDays[2].isChecked,
            weekDays[3].isChecked,
            weekDays[4].isChecked,
            weekDays[5].isChecked,
            weekDays[6].isChecked
        )


        val repeat = RecurringQuest(repeatType,checkedWeeks)

        val quest = Quest(name, description,date,repeat,type, difficulty)
        user.addQuest(quest)
        val changeFirebaseData = FirebaseData()
        changeFirebaseData.updateUserData(user)
        makeToast("Quest: " + quest.name + " created succesfully")
        resetFields()
    }

    private fun makeToast(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
    }

    private fun resetFields()
    {
        binding.createTaskName.setText("")
        binding.createTaskDescription.setText("")
        binding.repeatSpinner.setSelection(0)
        resetWeekFields()
        binding.createTaskDifficulty.setSelection(0)
        binding.createTaskType.setSelection(0)
    }

    private fun resetWeekFields()
    {
        for(w in weekDays)
            w.isChecked = false

    }


    @SuppressLint("SetTextI18n")
    private fun buttonSelectDate() {

        // Date Select Listener.
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                editTextDate!!.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                lastSelectedYear = year
                lastSelectedMonth = monthOfYear
                lastSelectedDayOfMonth = dayOfMonth
            }
        val datePickerDialog: DatePickerDialog?
        datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            lastSelectedYear,
            lastSelectedMonth,
            lastSelectedDayOfMonth
        )

        // Show
        datePickerDialog.show()
    }

}