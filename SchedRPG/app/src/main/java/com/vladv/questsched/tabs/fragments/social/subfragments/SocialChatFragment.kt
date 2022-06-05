package com.vladv.questsched.tabs.fragments.social.subfragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schedrpg.databinding.FragmentSocialChatBinding
import com.google.firebase.database.*
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.utilities.ChatMessages
import com.vladv.questsched.utilities.UserSocialProfile


class  SocialChatFragment(private val friendUserID:String,private val userProfile: UserSocialProfile) : Fragment() {

    private var _binding: FragmentSocialChatBinding? = null
    private val binding get() = _binding!!
    private val currentUserId: String = MyFragmentManager.firebaseAuth.uid!!
    private lateinit var rootRef: DatabaseReference
    private var messagesList : ArrayList<ChatMessages> = ArrayList()
    private lateinit var chatAdapter:SocialChatAdapter
    private lateinit var linearLayout: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialChatBinding.inflate(inflater, container, false)
        activity?.title = "Chat"

        rootRef = FirebaseDatabase.getInstance().reference


        binding.customProfileName.text = userProfile.username
        userProfile.avatar?.drawableFace?.let { binding.customProfileImage.setImageResource(it) }

        chatAdapter = SocialChatAdapter(userProfile.avatar!!.drawableFace!!,messagesList)
        linearLayout = LinearLayoutManager(context)
        binding.socialChatMessageList.layoutManager = linearLayout
        binding.socialChatMessageList.adapter = chatAdapter

        binding.socialSendMessageButton.setOnClickListener{
            sendMessage()
        }


        return binding.root
    }

    override fun onStart() {
        super.onStart()

        rootRef.child("Messages").child(currentUserId).child(friendUserID)
            .addChildEventListener(object : ChildEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val messages: ChatMessages? = dataSnapshot.getValue(ChatMessages::class.java)
                    if (messages != null) {
                        messagesList.add(messages)
                    }
                    chatAdapter.notifyDataSetChanged()
                    binding.socialChatMessageList.smoothScrollToPosition(
                        binding.socialChatMessageList.adapter!!.itemCount
                    )
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })


    }

    private fun sendMessage() {


        val messageText: String = binding.socialInputMessage.text.toString()

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(context, "No message written yet", Toast.LENGTH_SHORT).show()
        } else {
            val messageSenderRef = "Messages/$currentUserId/$friendUserID"
            val messageReceiverRef = "Messages/$friendUserID/$currentUserId"

            val userMessageKeyRef: DatabaseReference = rootRef.child("Messages")
                .child(currentUserId).child(friendUserID).push()
            val messagePushID = userMessageKeyRef.key
            val messageTextBody: MutableMap<String,Any> = HashMap()

            messageTextBody["message"] = messageText
            messageTextBody["from"] = currentUserId
            messageTextBody["to"] = friendUserID
            messageTextBody["messageID"] = messagePushID!!
//            messageTextBody["time"] = saveCurrentTime
//            messageTextBody["date"] = saveCurrentDate

            val messageBodyDetails: MutableMap<String,Any> = HashMap()

            messageBodyDetails["$messageSenderRef/$messagePushID"] = messageTextBody
            messageBodyDetails["$messageReceiverRef/$messagePushID"] = messageTextBody
            rootRef.updateChildren(messageBodyDetails)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Message Sent Successfully...",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                    binding.socialInputMessage.setText("")
                }
        }
    }


}