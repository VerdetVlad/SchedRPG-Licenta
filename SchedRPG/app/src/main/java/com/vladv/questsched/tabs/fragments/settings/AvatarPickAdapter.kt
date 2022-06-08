package com.vladv.questsched.tabs.fragments.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.schedrpg.R
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.Avatar
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.*


class AvatarPickAdapter(context: Context?, avatarList: ArrayList<Avatar>) :
    ArrayAdapter<Avatar?>(context!!, R.layout.fragment_avatar_pick_item, avatarList as List<Avatar?>)
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convtView = convertView
        val avatar = getItem(position)
        if (convtView == null) {
            convtView =
                LayoutInflater.from(context).inflate(R.layout.fragment_avatar_pick_item, parent, false)
        }

        val name = convtView!!.findViewById<TextView>(R.id.avatarPicName)
        val buttonSelect = convtView.findViewById<Button>(R.id.pickAvatarButton)
        val image = convtView.findViewById<ImageView>(R.id.pickAvatarImage)

        name.text = avatar!!.name
        avatar.drawableFace?.let { image.setImageResource(it) }

        buttonSelect.setOnClickListener{

            User.setAvatar(avatar)

            FirebaseData().updateUserData()
        }

        return convtView
    }


}