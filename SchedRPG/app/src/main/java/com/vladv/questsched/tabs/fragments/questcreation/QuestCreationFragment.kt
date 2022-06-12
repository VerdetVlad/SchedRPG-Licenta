package com.vladv.questsched.tabs.fragments.questcreation

import android.R
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentQuestCreationBinding
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.*
import com.vladv.questsched.utilities.Notification
import java.util.*


@Suppress("DEPRECATION")
class QuestCreationFragment : Fragment() {

    private var _binding: FragmentQuestCreationBinding? = null
    private val binding get() = _binding!!
    private var user = User()


    private var lastSelectedYear = 0
    private var lastSelectedMonth = 0
    private var lastSelectedDayOfMonth = 0
    private  var weekDays: ArrayList<CheckBox> = ArrayList()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestCreationBinding.inflate(inflater, container, false)
        activity?.title = "Quest Creation"

        MyFragmentManager.currentFragment = this

        val stats = arrayOf("Strength","Dexterity", "Constitution","Intelligence", "Wisdom", "Charisma")
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



        binding.buttonDate.setOnClickListener { buttonSelectDate(binding.createTextDate) }
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
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
                if(position!=0) {
                    binding.repeatUntileText.text = binding.createTextDate.text
                    if(position==2) {
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

        binding.creationNotifyCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
            {
                binding.creationTimePicker.visibility = View.VISIBLE
            }
            else {
                binding.creationTimePicker.visibility = View.GONE
            }
        }


        binding.createTaskSubmit.setOnClickListener {
            addTaskToFirebase()

            resetFields()
        }

        createNotificationChannel()

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
        val date = binding.createTextDate.text.toString()
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
        if(quest.initialDate!!.todayCheck()) user.addUnfinishedQuest(quest)



        val changeFirebaseUtilities = FirebaseUtilities()

        if(binding.creationNotifyCheckBox.isChecked)
        {
            scheduleNotification()
        }

        changeFirebaseUtilities.updateUserData(requireActivity(),
            "Quest: " + quest.name + " created succesfully",
            "Quest: " + quest.name + " creation failed: database connection error")

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

        binding.createTextDate.setText(myDateFormat(lastSelectedDayOfMonth,lastSelectedMonth,lastSelectedYear))
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

    private fun resetFields()
    {
        binding.createTaskName.setText("")
        binding.createTaskDescription.setText("")
        binding.createTaskDifficulty.setSelection(0)
        binding.createTaskType.setSelection(0)
        binding.repeatSpinner.setSelection(0)
        resetWeekFields()
        resetCalendarField()
        binding.repeatCheckBox.isChecked = false
        binding.creationNotifyCheckBox.isChecked = false

    }


    private fun scheduleNotification()
    {
        val intent = Intent(context, Notification::class.java)
        val name = binding.createTaskName.text.toString()
        val title = "Quest: $name reminder"
        var description = binding.createTaskDescription.text.toString()
        if(description == "") description = "reminder for $name"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, description)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun createNotificationChannel()
    {
        val name = "Notification Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun getTime(): Long
    {
        val minute = binding.creationTimePicker.minute
        val hour = binding.creationTimePicker.hour
        val date = MyDate(binding.createTextDate.text.toString())

        val day = date.day!!
        val month = date.month!!.minus(1)
        val year = date.year!!

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }




}