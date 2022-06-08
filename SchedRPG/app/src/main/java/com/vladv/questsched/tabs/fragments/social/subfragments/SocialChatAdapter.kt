package com.vladv.questsched.tabs.fragments.social.subfragments


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.utilities.ChatMessages
import com.example.schedrpg.R


class SocialChatAdapter(private val friendPicture:Int, private val messageList: List<ChatMessages>)
    : RecyclerView.Adapter<SocialChatAdapter.ViewHolder>() {


    private lateinit var userRef :DatabaseReference
    private val userID = MyFragmentManager.firebaseAuth.uid


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_social_chat_message_layout, parent, false)


        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val chatMessage = messageList[position]

        val fromUserID = chatMessage.from!!

        userRef = FirebaseDatabase.getInstance().reference.child("User").child(fromUserID)

        friendPicture.let { holder.friendProfileImage.setImageResource(it) }

        holder.friendText.visibility = View.GONE
        holder.friendProfileImage.visibility = View.GONE
        holder.userText.visibility = View.GONE

        if(fromUserID == userID)
        {
            holder.userText.visibility = View.VISIBLE
            holder.userText.text = chatMessage.message
        }
        else
        {
            holder.friendText.visibility = View.VISIBLE
            holder.friendProfileImage.visibility = View.VISIBLE
            holder.friendText.text = chatMessage.message

        }




    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return messageList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val friendProfileImage: ImageView = itemView.findViewById(R.id.friendChatProfileImage)
        val friendText: TextView = itemView.findViewById(R.id.friendChatTextMessage)
        val userText: TextView = itemView.findViewById(R.id.usetChatTextMessage)

    }
}