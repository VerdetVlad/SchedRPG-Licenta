package com.vladv.questsched.tabs .fragments.settings

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.schedrpg.R
import com.vladv.questsched.user.Avatar
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.FirebaseUtilities


class AvatarPickAdapter(context: Context?, avatarList: ArrayList<Avatar>) :
    ArrayAdapter<Avatar?>(context!!, R.layout.fragment_settings_avatar_pick_item, avatarList as List<Avatar?>)
{

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convtView = convertView
        val avatar = getItem(position)
        if (convtView == null) {
            convtView =
                LayoutInflater.from(context).inflate(R.layout.fragment_settings_avatar_pick_item, parent, false)
        }

        val name = convtView!!.findViewById<TextView>(R.id.avatarPicName)
        val requirements = convtView.findViewById<TextView>(R.id.avatarPickReq)
        val buttonSelect = convtView.findViewById<Button>(R.id.pickAvatarButton)
        val image = convtView.findViewById<ImageView>(R.id.pickAvatarImage)

        if(position == 4 || position == 5)
        {
            requirements.text = "Requirements: \n CHA >= 10 & STR >=10"
            if(User().stats?.statsLvl?.get(5)!! < 10
                || User().stats?.statsLvl?.get(0)!! < 10)
            {
                buttonSelect.visibility = View.GONE
            }
        }
        else
        {
            requirements.text = "Requirements: \n none"
        }

        name.text = avatar!!.name
        avatar.drawableFace?.let { image.setImageResource(it) }

        buttonSelect.setOnClickListener{

            User.setAvatar(avatar)

            FirebaseUtilities().updateUserData()


            SettingsFragment.auxActivity.recreate()
        }

        return convtView
    }


}