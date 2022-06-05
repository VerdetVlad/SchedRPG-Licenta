package com.vladv.questsched.tabs.fragments.questlistview

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.set
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentQuestEditBinding
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.questcreation.QuestCreationFragment
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.Recurrence
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.FirebaseData
import com.vladv.questsched.utilities.MyDate
import java.util.*


class EditQuestFragment(private val quest: Quest,private val position: Int) : Fragment() {

    private var _binding: FragmentQuestEditBinding? = null
    private val binding get() = _binding!!
    private var user = User()


    private var lastSelectedYear = 0
    private var lastSelectedMonth = 0
    private var lastSelectedDayOfMonth = 0
    private  var weekDays: ArrayList<CheckBox> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestEditBinding.inflate(inflater, container, false)

        fieldsSetUp()

        binding.editTaskName.setText(quest.name)
        binding.editTaskDescription.setText(quest.description)
        binding.editTextDate.setText(quest.initialDate!!.toStringDate())
        quest.repeat?.recurringFrequency?.let { binding.repeatSpinner.setSelection(it) }
        for(i in 0..6 ) weekDays[i].isChecked = quest.repeat!!.recurringDays!![i]
        binding.editRepeatUntileText.setText(quest.repeat!!.untilDate!!.toStringDate())
        if(quest.repeat!!.untilDate!!.toStringDate()=="12/12/9999")
            binding.editRepeatCheckBox.isChecked = true
        quest.type!!.let { binding.editTaskType.setSelection(it) }
        quest.difficulty!!.let { binding.editTaskDifficulty.setSelection(it) }




        val editBlock = binding.editQuestBlockLayout
        binding.editQuestFloatingButton.setOnClickListener {
            if(editBlock.visibility == View.VISIBLE) {
                binding.editQuestFloatingButton.setImageResource(R.drawable.quest_edit_save)
                binding.editQuestBlockLayout.visibility = View.GONE
            }
            else//save new data
            {
                binding.editQuestFloatingButton.setImageResource(R.drawable.quest_edit_edit)
                editQuestInFirebase()
                binding.editQuestBlockLayout.visibility = View.VISIBLE
            }
        }

        return binding.root
    }




    private fun fieldsSetUp()
    {
        val stats = arrayOf("Strength","Dexterity", "Constitution", "Wisdom", "Intelligence","Charisma")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            stats)
        binding.editTaskType.adapter = adapter

        val difficulty = arrayOf("very easy", "easy", "medium", "hard", "very hard")
        val adapter2 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            difficulty)
        binding.editTaskDifficulty.adapter = adapter2



        binding.buttonDate.setOnClickListener { buttonSelectDate(binding.editTextDate) }
        binding.editRepeatUntilButton.setOnClickListener { buttonSelectDate(binding.editRepeatUntileText) }
        resetCalendarField()



        val repeatOptions = arrayOf("never", "daily", "weekly", "monthly")
        val adapter3 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
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
                repeatSpinnerSelect()
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }


        binding.editRepeatCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
            {
                binding.editRepeatUntileText.visibility = View.GONE
                binding.editRepeatUntilButton.visibility = View.GONE
            }
            else {
                binding.editRepeatUntileText.visibility = View.VISIBLE
                binding.editRepeatUntilButton.visibility = View.VISIBLE
            }
        }
    }

    private fun editQuestInFirebase() {
        val name = binding.editTaskName.text.toString().trim { it <= ' ' }
        if (name.isEmpty()) {
            binding.editTaskName.error = "Name is required."
            binding.editTaskName.requestFocus()
            return
        }
        val description = binding.editTaskDescription.text.toString().trim { it <= ' ' }
        val difficulty = binding.editTaskDifficulty.selectedItemPosition
        val type = binding.editTaskType.selectedItemPosition
        val date = binding.editTextDate.text.toString()
        val repeatType = binding.repeatSpinner.selectedItemPosition
        val repeatUntil = binding.editRepeatUntileText.text.toString()
        val repeatForeverCheck = binding.editRepeatCheckBox.isChecked

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

        val auxQuest = Quest(name, description,date,repeat,type, difficulty)
        if(quest == auxQuest) return

        User.setQuestsAtIndex(auxQuest,position)
        val changeFirebaseData = FirebaseData()
        
        changeFirebaseData.updateUserData(requireActivity(),
            "Quest: " + quest.name + " edited succesfully",
            "Quest: " + quest.name + " editeding failed: database connection error")
    }

    private fun makeToast(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
    }

    private fun repeatSpinnerSelect() {
        val spinChoice = binding.repeatSpinner.selectedItemPosition
        if(spinChoice!=0) {
            if(spinChoice==2) {
                binding.creationLinLayout.visibility = View.VISIBLE
                binding.editRepeatConstrLayout.visibility = View.VISIBLE
            }
            else {
                binding.creationLinLayout.visibility = View.GONE
                binding.editRepeatConstrLayout.visibility = View.VISIBLE
            }
        }
        else {
            binding.creationLinLayout.visibility = View.GONE
            binding.editRepeatConstrLayout.visibility = View.GONE
            resetWeekFields()
        }
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

        if((month+1)<10) m = "0$m"
        if(day<10) d = "0$d"

        return "$d/$m/$y"
    }

}