package com.example.myapplication.screens.chatroom

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.datastructures.chatty.R
import com.example.myapplication.adapters.MessageAdapter
import com.example.myapplication.ui.main.Home
import com.example.myapplication.ui.main.Home.oldData
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.util.*

class ChatRoom : AppCompatActivity() {

    //local state and current user handle
    private var calleeUser: User? = null
    private var currentMsg: String? = null

    //required data structures
    private var messageList = ArrayList<Message>()
    private val lastMessage: MutableMap<String, String> = HashMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //to hide action bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_chat_room)

        //initializing data
        val sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE)
        val phone = sharedPreferences.getString("phone", null)
        val name = sharedPreferences.getString("name", null)
        val currentUser = User(name, phone)

        //get UI references
        val img = findViewById<CircleImageView>(R.id.recImg)
        val recName = findViewById<TextView>(R.id.TVname)
        val vidCall = findViewById<ImageButton>(R.id.vidCall)
        var notDone = false

        //Getting intent or saved instance data
        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras == null) {
                finish()
            } else {
                calleeUser = User(extras.getString("name"), extras.getString("RecPhone"))
                recName.text = calleeUser!!.name
                if (extras.getString("image") != null) {
                    Glide.with(this).load(extras.getString("image"))
                        .placeholder(org.jitsi.meet.sdk.R.drawable.images_avatar).into(img)
                }
            }
        } else {
            calleeUser = User(
                savedInstanceState.getSerializable("name") as String?,
                savedInstanceState.getSerializable("RecPhone") as String?
            )
        }
        Timber.tag(ContentValues.TAG)
            .d("%s%s", "Users : >> " + calleeUser!!.profile + " ", calleeUser!!.name)

        //database references
        val currentDoc = getDoc(currentUser.profile, calleeUser!!.profile)
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("chatRooms").document(currentDoc)
        val collRef = docRef.collection("Messages")

        recycler_chat.layoutManager = LinearLayoutManager(this)

        //get old messages
        messageList.clear()
        runBlocking {
            val getting = launch {
                if (phone != null && !Home.hasRetrieved) {
                    getOldMsg(collRef , phone)
                }
                else{
                    notDone =true
                }
            }
            getting.join()
            println(
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@ Getting Old Messages, count: " + messageList.size +
                        " , Has ret = " + Home.hasRetrieved
            )
        }
        if (notDone){
            messageList = ArrayList<Message>() //Home.oldData.clone() as ArrayList<Message>
            recycler_chat.adapter = MessageAdapter(this , messageList , phone )

            for (i in 0 until (oldData.size/ oldData.size)){
                messageList.add(oldData[i])
                recycler_chat.adapter!!.notifyItemInserted(i)
            }
        }

        //listen to chat
        listenToChat(collRef)

        //Enable video calls
        vidCall.setOnClickListener { view: View? ->
            if (currentDoc.isNotEmpty()) {
                val options = JitsiMeetConferenceOptions.Builder()
                    .setRoom("CarbonVideoCall$currentDoc")
                    .build()
                JitsiMeetActivity.launch(this, options)
            } else Snackbar.make(view!!, "Invalid Room ID", BaseTransientBottomBar.LENGTH_SHORT)
                .show()
        }

        //Send button action
        sendBtn.setOnClickListener { view: View? ->
            //Remove extra spaces and break lines
            currentMsg = msgTxt.getText().toString().trim { it <= ' ' }
            if (currentMsg != "") {
                // Add a new message object to the end of messages arraylist
                val tmp = Message(
                    currentMsg,
                    Timestamp(System.currentTimeMillis()).toString(), currentUser.profile
                )
                updateState(tmp)
                //serialize encrypted message in the hashmap
                lastMessage["text"] = encryptMsg(tmp.text)
                lastMessage["time"] = tmp.time
                lastMessage["user"] = encryptMsg(tmp.user)

                //Send message hashmap to fireStore and handle success / failure
                val msgDoc: String = Timestamp(System.currentTimeMillis())
                    .toString().replace("\\s".toRegex(), "")
                collRef.document(msgDoc)
                    .set(lastMessage, SetOptions.merge())
                    .addOnSuccessListener { aVoid: Void? ->
                        Timber.tag(ContentValues.TAG).d("DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener { e: Exception? ->
                        Timber.tag(ContentValues.TAG).w(e, "Error writing document")
                    }
                //empty the message holder to receive the next message
                msgTxt.setText("")
            }
        }
    }



    private fun getDoc(num1: String, num2: String): String {
        return if (num1.compareTo(num2) > 0) num1 + num2 else num2 + num1
    }

    private fun updateState(msg: Message) {
        messageList.add(msg)
        recycler_chat.adapter!!.notifyItemInserted(messageList.size)
        recycler_chat!!.smoothScrollToPosition(recycler_chat.adapter!!.itemCount)
    }

    private fun encryptMsg(msg: String): String {
        return Base64.getEncoder()
            .encodeToString(msg.toByteArray(StandardCharsets.UTF_8))
    }

    private fun listenToChat(collRef: CollectionReference) {
        collRef.addSnapshotListener { snapshots: QuerySnapshot?, e: FirebaseFirestoreException? ->
            if (e != null) {
                Timber.tag(ContentValues.TAG).w(e, "listen:error")
                return@addSnapshotListener
            }
            if (snapshots != null) for (dc in snapshots.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        val msg = dc.document.toObject(
                            Message::class.java
                        )
                        try {
                            if (msg.time != messageList[messageList.size - 1].time) {
                                updateState(
                                    Message(
                                        String(Base64.getDecoder().decode(msg.text)),
                                        msg.time,
                                        String(Base64.getDecoder().decode(msg.user))
                                    )
                                )
                            }
                        } catch (x: Exception) {
                            Timber.tag(ContentValues.TAG).d("Empty messages")
                        }
                        Timber.tag(ContentValues.TAG).d("New Message: %s", dc.document.data)
                    }
                    DocumentChange.Type.MODIFIED -> Timber.tag(ContentValues.TAG)
                        .d("Modified Message: %s", dc.document.data)
                    DocumentChange.Type.REMOVED -> Timber.tag(ContentValues.TAG)
                        .d("Removed Message: %s", dc.document.data)
                }
            }
        }
    }

    private fun getOldMsg(collRef: CollectionReference , phone: String) {
        Home.hasRetrieved = true;
        messageList = ArrayList<Message>()
        recycler_chat.adapter = MessageAdapter(this , messageList , phone )
        recycler_chat.adapter!!.notifyDataSetChanged()
        println("Item Count ${recycler_chat.adapter!!.itemCount}")
        for ( i in 0 until recycler_chat.adapter!!.itemCount){
            recycler_chat.adapter!!.notifyItemRemoved(i)
        }

        var count = 1
        collRef.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    println("Count $count")
                    count++
                    val msg = document.toObject(
                        Message::class.java
                    )
                    messageList.add(
                        Message(
                            String(Base64.getDecoder().decode(msg.text)),
                            msg.time,
                            String(Base64.getDecoder().decode(msg.user))
                        )
                    )
                    oldData.add(
                        Message(
                            String(Base64.getDecoder().decode(msg.text)),
                            msg.time,
                            String(Base64.getDecoder().decode(msg.user))
                        )
                    )
                    recycler_chat.adapter!!.notifyItemInserted(messageList.size)
                }
            } else {
                Timber.tag(ContentValues.TAG)
                    .d(task.exception, "Error getting Messages documents: ")
            }
        }
    }

}