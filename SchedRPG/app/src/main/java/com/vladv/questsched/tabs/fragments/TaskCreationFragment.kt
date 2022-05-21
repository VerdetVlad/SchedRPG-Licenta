package com.vladv.questsched.tabs.fragments

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentTaskCreationBinding
import com.vladv.questsched.utilities.FirebaseData
import com.vladv.questsched.utilities.Quest
import com.vladv.questsched.utilities.Recurrence
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.MyDate
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class TaskCreationFragment : Fragment() {

    private var _binding: FragmentTaskCreationBinding? = null
    private val binding get() = _binding!!
    private var user = User()


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



        binding.buttonDate.setOnClickListener { buttonSelectDate(binding.editTextDate) }
        binding.repeatUntilButton.setOnClickListener { buttonSelectDate(binding.repeatUntileText) }
        resetCalendarField()



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
                if(spinChoice!=0) {
                    if(spinChoice==2) {
                        binding.creationLinLayout.visibility = View.VISIBLE
                        binding.repeatConstrLayout.visibility = View.VISIBLE
                    }
                    else {
                        binding.creationLinLayout.visibility = View.GONE
                        binding.repeatConstrLayout.visibility = View.VISIBLE
                    }
                }
                else {
                    binding.creationLinLayout.visibility = View.GONE
                    binding.repeatConstrLayout.visibility = View.GONE
                    resetWeekFields()
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }


        binding.repeatCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
            {
                binding.repeatUntileText.visibility = View.GONE
                binding.repeatUntilButton.visibility = View.GONE
            }
            else {
                binding.repeatUntileText.visibility = View.VISIBLE
                binding.repeatUntilButton.visibility = View.VISIBLE
            }
        }


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
        val repeatType = binding.repeatSpinner.selectedItemPosition
        val repeatUntil = binding.repeatUntileText.text.toString()
        val repeatForeverCheck = binding.repeatCheckBox.isChecked

        val checkedWeeks = arrayListOf(
            weekDays[0].isChecked,
            weekDays[1].isChecked,
            weekDays[2].isChecked,
            weekDays[3].isChecked,
            weekDays[4].isChecked,
            weekDays[5].isChecked,
            weekDays[6].isChecked
        )

        val untilDate = when {
            repeatType == 0 -> MyDate(date)
            repeatForeverCheck -> MyDate("12 12 9999")
            else -> MyDate(repeatUntil)
        }

        val repeat = Recurrence(repeatType,checkedWeeks, untilDate)

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
        resetCalendarField()
        resetWeekFields()
        binding.repeatUntileText.setText("")
        binding.repeatCheckBox.isChecked = false
        binding.createTaskDifficulty.setSelection(0)
        binding.createTaskType.setSelection(0)
    }

    private fun resetWeekFields()
    {
        for(w in weekDays)
            w.isChecked = false

    }

    @SuppressLint("SetTextI18n")
    private fun resetCalendarField()
    {
        val c = Calendar.getInstance()
        lastSelectedYear = c[Calendar.YEAR]
        lastSelectedMonth = c[Calendar.MONTH]
        lastSelectedDayOfMonth = c[Calendar.DAY_OF_MONTH]

        binding.editTextDate.setText(myDateFormat(lastSelectedDayOfMonth,lastSelectedMonth,lastSelectedYear))
    }


    @SuppressLint("SetTextI18n")
    private fun buttonSelectDate(editText: EditText) {

        // Date Select Listener.
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                editText.setText(myDateFormat(dayOfMonth,monthOfYear,year))
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


    private fun myDateFormat(day:Int,month:Int,year:Int) : String
    {
        var d = day.toString()
        var m = (month+1).toString()
        val y = year.toString()

        if((month+1)<10) m = "0" + m;
        if(day<10) d = "0" + d;

        return "$d/$m/$y"
    }
}