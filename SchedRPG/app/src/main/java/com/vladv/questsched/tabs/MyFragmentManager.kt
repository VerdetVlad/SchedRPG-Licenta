package com.vladv.questsched.tabs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.schedrpg.R
import com.example.schedrpg.databinding.ActivityFragmentManagerBinding
import com.example.schedrpg.databinding.PopUpQuestBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vladv.questsched.authentification.LogIn
import com.vladv.questsched.tabs.fragments.calendar.CalendarDayFragment
import com.vladv.questsched.tabs.fragments.calendar.CalendarFragment
import com.vladv.questsched.tabs.fragments.home.HomeNavFragment
import com.vladv.questsched.tabs.fragments.questcreation.QuestCreationFragment
import com.vladv.questsched.tabs.fragments.questlistview.EditQuestFragment
import com.vladv.questsched.tabs.fragments.questlistview.QuestListFragment
import com.vladv.questsched.tabs.fragments.settings.AvatarPickFragment
import com.vladv.questsched.tabs.fragments.settings.ChangePasswordFragment
import com.vladv.questsched.tabs.fragments.settings.SettingsFragment
import com.vladv.questsched.tabs.fragments.social.SocialNavFragment
import com.vladv.questsched.tabs.fragments.social.subfragments.SocialChatFragment
import com.vladv.questsched.tabs.fragments.social.subfragments.SocialUserProfile
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.FinishedQuestData
import com.vladv.questsched.utilities.FirebaseUtilities


class MyFragmentManager : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener
    lateinit var binding: ActivityFragmentManagerBinding

    private val timeInterval = 2000
    private var backPressed:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentManagerBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)



        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        if(currentUser!= null){
            if(!currentUser.isEmailVerified){
                Toast.makeText(this, "Failed to LogIn. Please confirm email first.", Toast.LENGTH_LONG).show()
                firebaseAuth.signOut()
            }
        }


        authStateListener = FirebaseAuth.AuthStateListener  { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                val intent = Intent(this, LogIn::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
                finish()
            }
        }

        if(null == savedInstanceState) {
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout,
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout
                )
                replace(binding.flFragment.id, HomeNavFragment())
            }

        }



        configureDrawer()
        configureToolbar()


        stopLoading()

    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(this.authStateListener)

        startLoading()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference(User::class.java.simpleName)

        if(firebaseUser == null) return
        val userID = firebaseUser.uid
        reference.child(userID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userData = snapshot.getValue(User::class.java)


                stopLoading()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong when retrieving data: $error", Toast.LENGTH_LONG).show()
                stopLoading()
            }
        })
    }


    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this.authStateListener)
    }




    private fun configureDrawer()
    {
        val drawerLayout : DrawerLayout  = binding.drawerLayout
        val navView : NavigationView = binding.navView
        val navHeader = navView.getHeaderView(0)


        val userName = navHeader.findViewById(R.id.user_name) as TextView
        val mailAddress = navHeader.findViewById(R.id.nav_mail_adress) as TextView
        val avatarProfile = navHeader.findViewById(R.id.avatarProfilePic) as ImageView



        userName.text = userData!!.username
        mailAddress.text = userData!!.email
        userData!!.avatar?.drawableFace?.let { avatarProfile.setImageResource(it) }

        avatarProfile.setOnClickListener {
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout,
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout
                )
                clearBackStack()
                replace(binding.flFragment.id, SettingsFragment())

                drawerLayout.closeDrawers()
            }
        }

        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    clearBackStack()
                    replace(binding.flFragment.id, HomeNavFragment())
                    drawerLayout.closeDrawers()
                }

                R.id.nav_new_quest -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    clearBackStack()
                    replace(binding.flFragment.id, QuestCreationFragment())
                    drawerLayout.closeDrawers()
                }


                R.id.nav_quest_list -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    clearBackStack()
                    replace(binding.flFragment.id, QuestListFragment())
                    drawerLayout.closeDrawers()
                }

                R.id.nav_calendar -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    clearBackStack()
                    replace(binding.flFragment.id, CalendarFragment())

                    drawerLayout.closeDrawers()
                }

                R.id.nav_setting -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    clearBackStack()
                    replace(binding.flFragment.id, SettingsFragment())

                    drawerLayout.closeDrawers()
                }

                R.id.nav_friends -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    clearBackStack()
                    replace(binding.flFragment.id, SocialNavFragment())

                    drawerLayout.closeDrawers()
                }




                R.id.nav_logout ->{
                    clearBackStack()
                    FirebaseUtilities().removeTokenAndLogOut(this)
                }


            }

            true
        }
    }

    private fun configureToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val actionbar: ActionBar = supportActionBar!!
        actionbar.setHomeAsUpIndicator(R.drawable.sidenav_display_drawer)
        actionbar.setDisplayHomeAsUpEnabled(true)

        actionbar.setDisplayShowHomeEnabled(true)
    }

    private fun clearBackStack()
    {
        val fm = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){

            return true


        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        if(currentFragment::class == SocialChatFragment::class
            || currentFragment::class == SocialUserProfile::class) {
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout,
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout
                )
                clearBackStack()
                replace(binding.flFragment.id, SocialNavFragment())
            }
            return
        }

        if(currentFragment::class == ChangePasswordFragment::class
            || currentFragment::class == AvatarPickFragment::class) {
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout,
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout
                )
                clearBackStack()
                replace(binding.flFragment.id, SettingsFragment())
            }
            return
        }


        if(currentFragment::class == EditQuestFragment::class) {
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout,
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout
                )
                clearBackStack()
                replace(binding.flFragment.id, QuestListFragment())
            }
            return
        }

        if(currentFragment::class == CalendarDayFragment::class) {
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout,
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout
                )
                clearBackStack()
                replace(binding.flFragment.id, CalendarFragment())
            }
            return
        }


        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if(backStackEntryCount != 0) {
            super.onBackPressed()
            return
        }

        if(backPressed + timeInterval > System.currentTimeMillis())
        {
            super.onBackPressed()
            return
        }
        else{
            Toast.makeText(baseContext, "Press Back Again to Exit App", Toast.LENGTH_SHORT ).show()
        }
        backPressed = System.currentTimeMillis()
    }


    private fun startLoading()
    {
        binding.progressBarContainer.visibility = View.VISIBLE
    }

    private fun stopLoading()
    {
        binding.progressBarContainer.visibility = View.GONE
    }


    companion object {
        var userData: User? = User()
        var currentFragment : Fragment = HomeNavFragment()

        lateinit var firebaseAuth : FirebaseAuth


        @SuppressLint("SetTextI18n")
        fun createQuestPopUp(quest: Quest, context: Context)
        {
            val dialog: AlertDialog?
            val dialogBuilder: AlertDialog.Builder = context.let { AlertDialog.Builder(it, R.style.AlertDialogStyle) }
            val inflater = LayoutInflater.from(context)
            val auxBinding = PopUpQuestBinding.inflate(inflater)
            auxBinding.popUpQuestName.text = quest.name
            auxBinding.popUpQuestType.text = quest.typeStringValue()
            auxBinding.popUpQuestDificulty.text = quest.difficultyStringValue()
            auxBinding.popUpStartDate.text = "Begin: " + quest.initialDate?.toStringDate()

            if(quest.repeat?.recurringFrequency!=0)
            {
                auxBinding.popUpRepeatLayout.visibility=View.VISIBLE
                auxBinding.popUpRepeatType.text = "Repeat type: " + quest.repeat!!.frequencyToString()
                if(quest.repeat?.recurringFrequency==2)
                {
                    auxBinding.popUpRepeatDays.visibility = View.VISIBLE
                    auxBinding.popUpRepeatDays.text = "On: " + quest.repeat!!.recurringDaysToString()
                }
                auxBinding.popUpRepeatUntil.text = "End: " + quest.repeat!!.untilDate!!.toStringDate()
            }


            if(quest.description!="") {
                auxBinding.popUpQuestDescription.visibility = View.VISIBLE
                auxBinding.popUpQuestDescription.text = "Description: \n" + quest.description
            }

            dialogBuilder.setView(auxBinding.root)
            dialog = dialogBuilder.create()
            dialog.show()

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes ?: return)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            dialog.window!!.attributes = layoutParams

            auxBinding.closeQuestPopUpButton.setOnClickListener{
                dialog.dismiss()
            }
        }

        @SuppressLint("SetTextI18n")
        fun createQuestPopUp(quest: FinishedQuestData, context: Context)
        {
            val dialog: AlertDialog?
            val dialogBuilder: AlertDialog.Builder = context.let { AlertDialog.Builder(it, R.style.AlertDialogStyle) }
            val inflater = LayoutInflater.from(context)
            val auxBinding = PopUpQuestBinding.inflate(inflater)
            auxBinding.popUpQuestName.text = quest.name
            auxBinding.popUpQuestType.text = quest.type
            auxBinding.popUpQuestDificulty.text = quest.difficulty

            auxBinding.popUpStartDate.visibility = View.GONE


            if(quest.description!="") {
                auxBinding.popUpQuestDescription.visibility = View.VISIBLE
                auxBinding.popUpQuestDescription.text = "Description: \n" + quest.description
            }

            dialogBuilder.setView(auxBinding.root)
            dialog = dialogBuilder.create()
            dialog.show()

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes ?: return)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            dialog.window!!.attributes = layoutParams

            auxBinding.closeQuestPopUpButton.setOnClickListener{
                dialog.dismiss()
            }
        }

    }

}