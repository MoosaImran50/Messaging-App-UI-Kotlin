package com.example.assignment1

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class ConversationActivity : AppCompatActivity() {

    private val response = mutableListOf<String>("hello", "Hey", "Hi", "How may I help you?", "OK", "Great", "Got it!", "Hmm", "Affirmative", "Sure")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversation_main)

        val contactID = intent.getIntExtra("ID", 0)
        val contactName = intent.getStringExtra("NAME")
        getSupportActionBar()?.setTitle(contactName)

        val db =  DatabaseHelper(this)
        val messagesList = db.readMessage(contactID)

        val recyclerView = findViewById<RecyclerView>(R.id.myRecyclerView)
        recyclerView.setBackgroundColor(Color.BLACK)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyMessageRecyclerViewAdapter(messagesList)

        val textBox = findViewById<EditText>(R.id.myTextBox)
        val sendButton = findViewById<Button>(R.id.mySendButton)
        var enteredMessage = ""

        recyclerView.scrollToPosition(messagesList.size - 1)

        sendButton.setOnClickListener {
            enteredMessage = textBox.text.toString().trim()
            if (enteredMessage == "") {
                Toast.makeText(
                    this@ConversationActivity,
                    "Text field can not be left blank!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                val message = enteredMessage
                val timeFormat = SimpleDateFormat("h:mm a")
                val currentTime = timeFormat.format(Date())

                val random = (0..9).random()
                val randomResponse = response[random]

                messagesList.add(Message(contactID, "Me", message, currentTime))
                messagesList.add(Message(contactID, contactName!!, randomResponse, currentTime))

                textBox.text.clear()
                recyclerView.scrollToPosition(messagesList.size - 1)

                db.addMessage(Message(contactID, "Me", message, currentTime))
                db.addMessage(Message(contactID, contactName, randomResponse, currentTime))
            }
        }

    }
}