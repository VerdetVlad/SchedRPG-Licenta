package com.vladv.questsched.tabs.fragments.home.subfragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentHomeQuestsBinding
import com.example.schedrpg.databinding.PopUpFailedQuestsBinding
import com.vladv.questsched.tabs.fragments.home.HomeNavFragment
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.FirebaseUtilities
import com.vladv.questsched.utilities.MyDate
import java.util.*


class HomeQuestsFragment : Fragment() {

    private var _binding: FragmentHomeQuestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendar:Calendar


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeQuestsBinding.inflate(inflater, container, false)

        calendar = Calendar.getInstance()
        date = MyDate(calendar)

        activity?.title = "Quest's today ${date.toStringDate()}"
        HomeNavFragment.currentFragment = HomeQuestsFragment()


        return binding.root

    }


    override fun onStart() {
        super.onStart()

        auxActivity = activity
        val user = User()


        if(user.lastLogIn!!.logInDate!!.todayCheck())
            auxQuestArray = ArrayList(user.lastLogIn!!.unfinishedQuests)
        else {

            val oldDate = user.lastLogIn!!.logInDate!!
            val currDate = MyDate(Calendar.getInstance())


            if(!User().quests.isNullOrEmpty()) {
                var questsFailed = 0
                var questFailedNames =""

                if(user.lastLogIn!!.unfinishedQuests.isNotEmpty()) {
                    oldDate.increaseDayByOne()

                    while(user.lastLogIn!!.unfinishedQuests.isNotEmpty())
                    {
                        val q = user.lastLogIn!!.unfinishedQuests[0]
                        User().addToQuestHistory(oldDate, q, false)
                        User().abandonQuest(q)
                        questsFailed++
                        questFailedNames += "'" + q.name + "'" + " failed on " + "\n" + oldDate.toStringDate() + "\n\n"
                    }

                }

                while (oldDate != currDate) {
                    for (q in User().quests!!) if (q.wasValidDate(oldDate)) {
                        questsFailed++
                        questFailedNames+= "'" + q.name + "'" + " failed on " + "\n"+ oldDate.toStringDate() +"\n\n"
                        User().addToQuestHistory(oldDate,q,Math.random() < 0.5)
//                        User().abandonQuest(q)
                    }
                    oldDate.increaseDayByOne()
                }

                if(questsFailed>0) failedQuestsPopUp(questsFailed,questFailedNames)
            }

            val questList = user.quests?.filter { quest ->
                quest.validDate(date)
            }
            auxQuestArray = questList?.let { ArrayList(it) }

            user.lastLogIn!!.logInDate = date
            if(auxQuestArray!= null) user.lastLogIn!!.unfinishedQuests = auxQuestArray as ArrayList<Quest>
            FirebaseUtilities().updateUserData()

        }

        if(!auxQuestArray.isNullOrEmpty()) {
            binding.noQuestTextView.isVisible = false
            listAdapter = HomeQuestsAdapter(requireContext(), auxQuestArray)
            listView = binding.questListView
            listView!!.adapter = listAdapter

        }
        else
        {
            binding.noQuestTextView.isVisible = true
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        var listView: ListView? = null

        var auxQuestArray:ArrayList<Quest>? =null
        var auxActivity: FragmentActivity? = null

        lateinit var date: MyDate


        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var listAdapter: HomeQuestsAdapter? = null

        fun removeItem(quest: Quest,status:String) {
            auxQuestArray?.remove(quest)
            listAdapter?.notifyDataSetChanged()
            FirebaseUtilities().updateUserData(auxActivity!!, "Quest: ${quest.name} $status", "Error: ${quest.name} not $status")


        }

    }


    @SuppressLint("SetTextI18n")
    fun failedQuestsPopUp(number:Int, names:String)
    {
        val dialog: AlertDialog?

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
        val inflater = LayoutInflater.from(context)
        val auxBinding = PopUpFailedQuestsBinding.inflate(inflater)
        auxBinding.popUpNumberQuestsFailed.text = "Quests failed: $number"
        auxBinding.popUpQuestFailedNames.text = names

        dialogBuilder.setView(auxBinding.root)
        dialog = dialogBuilder.create()
        dialog.show()

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes ?: return)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams

        auxBinding.closeQuestFailedPopUpButton.setOnClickListener{
            dialog.dismiss()
        }

    }





}