package com.example.bcs421_project2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StatsActivity : AppCompatActivity() {
    private lateinit var earnings: TextView
    private lateinit var results: TextView
    private lateinit var gameDataHandler: GameDataHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        gameDataHandler = GameDataHandler(this)

        earnings = findViewById(R.id.earnings)
        results = findViewById(R.id.results)

        //gets total and correct answers from question activity
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val savedTotal = sharedPreferences.getInt("runningTotal", 0)
        val savedCorrect = sharedPreferences.getInt("finalCorrect", 0)
        val (runningTotal, numCorrect) = gameDataHandler.loadGameHistory()

        //displays earnings and correct answers
        earnings.text = "You earned $savedTotal Credits this game!"
        results.text = "You got $savedCorrect out of 9 questions correct!"

        val replayBtn = findViewById<Button>(R.id.replayBtn)

        //brings user back to the splash activity and restarts the game
        replayBtn.setOnClickListener {
            val intent = Intent(this@StatsActivity, SplashActivity::class.java)
            startActivity(intent)

            finish()
        }

    }

}