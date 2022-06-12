package com.vladv.questsched.tabs.fragments.social.subfragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.ChatMessages


class  SocialChatFragment : Fragment {

    private lateinit var friendUserID: String
    private lateinit var username:String
    private var profilePicture: Int = 0
    private lateinit var lastMessage:DatabaseReference
    private lateinit var notificationRef:DatabaseReference
    private lateinit var messageListener:ChildEventListener
    private lateinit var seenMessageListener:ChildEventListener

    private var friendSeenStatus:Boolean = false

    constructor(){
        this.currentUserId = MyFragmentManager.firebaseAuth.uid!!
        this.messagesList = ArrayList()
    }


    constructor(friendUserID: String, username: String, profilePicture: Int) : super() {
        this.friendUserID = friendUserID
        this.username = username
        this.profilePicture = profilePicture
        this.currentUserId = MyFragmentManager.firebaseAuth.uid!!
        this.messagesList = ArrayList()
    }

    private var _binding: FragmentSocialChatBinding? = null
    private val binding get() = _binding!!
    private val currentUserId: String
    private lateinit var rootRef: DatabaseReference
    private var messagesList : ArrayList<ChatMessages>
    private lateinit var chatAdapter:SocialChatAdapter
    private lateinit var linearLayout: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialChatBinding.inflate(inflater, container, false)
        activity?.title = "Chat"

        rootRef = FirebaseDatabase.getInstance().reference
        notificationRef = FirebaseDatabase.getInstance().reference.child("MessageNotifications");

        MyFragmentManager.currentFragment = this

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if(!this::friendUserID.isInitialized) return

        binding.customProfileName.text = username
        profilePicture.let { binding.customProfileImage.setImageResource(it) }

        chatAdapter = SocialChatAdapter(profilePicture,messagesList)
        linearLayout = LinearLayoutManager(context)
        binding.socialChatMessageList.layoutManager = linearLayout
        binding.socialChatMessageList.adapter = chatAdapter

        lastMessage = FirebaseDatabase.getInstance().reference.child("Messages").child(currentUserId).child(friendUserID)


        binding.socialSendMessageButton.setOnClickListener{
            sendMessage()
        }

        messageListener = rootRef.child("Messages").child(currentUserId).child(friendUserID)
            .addChildEventListener(object : ChildEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val messages: ChatMessages? = dataSnapshot.getValue(ChatMessages::class.java)
                    if (messages != null) {
                        lastMessage.child(messages.messageID.toString()).child("seenStatus").setValue("seen")
                        messagesList.add(messages)
                    }
                    chatAdapter.notifyDataSetChanged()
                    binding.socialChatMessageList.smoothScrollToPosition(
                        binding.socialChatMessageList.adapter!!.itemCount
                    )
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                }
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })

        seenMessageListener = rootRef.child("Messages").child(friendUserID).child(currentUserId).orderByKey().limitToLast(1)
            .addChildEventListener(object : ChildEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val messages: ChatMessages? = dataSnapshot.getValue(ChatMessages::class.java)
                    if (messages != null) {
                        if(messages.seenStatus=="unseen") {
                            friendSeenStatus = false
                            binding.chatMessageSeenStatusText.visibility = View.VISIBLE
                        }
                        else {
                            friendSeenStatus = true
                            binding.chatMessageSeenStatusText.visibility = View.GONE
                        }
                    }
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    val messages: ChatMessages? = dataSnapshot.getValue(ChatMessages::class.java)
                    if (messages != null) {
                        if(messages.seenStatus=="unseen") {
                            friendSeenStatus = false
                            binding.chatMessageSeenStatusText.visibility = View.VISIBLE
                        }
                        else {
                            friendSeenStatus = true
                            binding.chatMessageSeenStatusText.visibility = View.GONE
                        }
                    }
                }
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })


    }

    private fun sendMessage() {


        val messageText: String = binding.socialInputMessage.text.toString().trim { it <= ' ' }

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
            messageTextBody["seenStatus"] = "unseen"
//            messageTextBody["time"] = saveCurrentTime
//            messageTextBody["date"] = saveCurrentDate

            val messageBodyDetails: MutableMap<String,Any> = HashMap()

            messageBodyDetails["$messageSenderRef/$messagePushID"] = messageTextBody
            messageBodyDetails["$messageReceiverRef/$messagePushID"] = messageTextBody
            rootRef.updateChildren(messageBodyDetails)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendNotification(messageText, messagePushID)
                    }
                    else
                    {
                        Toast.makeText(context, "Message Failed to send", Toast.LENGTH_SHORT).show()
                    }
                    binding.socialInputMessage.setText("")
                }
        }
    }

    private fun sendNotification(message:String, messageID:String)
    {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if(!friendSeenStatus) {
                    val chatNotificationMap: HashMap<String, String> = HashMap()
                    chatNotificationMap["from"] = User().username!!
                    chatNotificationMap["content"] = message

                    notificationRef.child(friendUserID).push()
                        .setValue(chatNotificationMap)
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(context, "Failed to notify user.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            },
            1000 // value in milliseconds
        )

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(!this::friendUserID.isInitialized) return
        outState.putString("friendUserID", friendUserID)
        outState.putString("friendUserName", username)
        outState.putInt("friendPictureID", profilePicture)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {

            friendUserID = savedInstanceState.getString("friendUserID")!!
            username = savedInstanceState.getString("friendUserName")!!
            profilePicture = savedInstanceState.getInt("friendPictureID")
        }
    }

    override fun onDetach() {
        super.onDetach()

        rootRef.child("Messages").child(currentUserId).child(friendUserID).removeEventListener(messageListener)
    }




}