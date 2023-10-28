package com.example.bcs421_project2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onlogin)

        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        val savedFName = sharedPreferences.getString("newfName", "")
        val savedLName = sharedPreferences.getString("newlName", "")

        val greeting = "Hello $savedFName $savedLName! Welcome to the game!"
        val onLoginGreeting = findViewById<TextView>(R.id.onLoginGreeting)
        onLoginGreeting.text = greeting

        val rules = "Rules of the game:\n Answer all questions!\n Some are multiple choice," +
                " some are multiple answer.\n After making a choice hit confirm or cancel.\n" +
                "Have fun!"
        val textViewRules = findViewById<TextView>(R.id.textViewRules)
        textViewRules.text = rules

        val startGameButton = findViewById<Button>(R.id.startGameButton)

        startGameButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }


    }
}