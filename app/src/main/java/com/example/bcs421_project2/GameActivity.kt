package com.example.bcs421_project2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    data class Question(
        val text: String,
        val options: List<String>,
        val correctOptionIndices: List<Int>,
        val payout: Int
    )

    private val questions = mutableListOf(
        Question(
            "Who was introduced as Luke Skywalker's best friend in Episode IV?",
            listOf("Han Solo", "Biggs Darklighter", "Obi-Wan Kenobi", "Jed Porkins"), listOf(2),
            100
        ), Question(
            "What is type of freighter is the Millennium Falcon?",
            listOf("VCX-100", "HWK-290", "G9 Rigger", "YT-1300"), listOf(4), 250
        ), Question(
            "Who were the twin children of Padmé Amidala?",
            listOf("Luke Skywalker", "Leia Organa", "Han Solo", "Obi-Wan Kenobi"),
            listOf(1, 2),
            500
        ), Question(
            "What planet did Kylo Ren find the 'resurrected' Emperor Palpatine on?",
            listOf("Dathomir", "Lothal", "Endor", "Exogol"), listOf(4), 1000
        ), Question(
            "What was the name of Anakin Skywalker's mother?",
            listOf("Shmi", "Ahsoka", "Leia", "Aayla"), listOf(1), 2000
        ), Question(
            "Which planets did Anakin Skywalker grow up on?",
            listOf("Tatooine", "Alderaan", "Coruscant", "Endor"),
            listOf(1, 3),
            4500
        ), Question(
            "What infamous legends character was introduced into the new canon in Rebels?",
            listOf("Starkiller", "Jacen Solo", "Thrawn", "Kyle Katarn"), listOf(3),
            9000
        ), Question(
            "'Many __ died to bring us this information', What race of aliens died to provide" +
                    " the rebellion with the Death Star II plans?", listOf(
                "Ithorians", "Lombax",
                "Trandoshans", "Bothans"
            ), listOf(4), 18500
        ), Question(
            "What bounty hunter from the prequels and The Clone Wars was revealed to be killed" +
                    " by one of the protagonists from Solo?",
            listOf("Zuckuss", "Zam Wessell", "Jango Fett", "Aurra Sing"),
            listOf(4),
            37000
        )
    )
    private lateinit var questionTextView: TextView
    private lateinit var checkBoxesLayout: LinearLayout
    private lateinit var answerGroup: RadioGroup
    private lateinit var confirmButton: Button
    private lateinit var earnings: TextView
    private lateinit var gameDataHandler: GameDataHandler

    private var currQIndex = 0
    private var runningTotal = 0
    private var numCorrect = 0
    private var selectedRBtn: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        questionTextView = findViewById(R.id.questionTextView)
        checkBoxesLayout = findViewById(R.id.checkBoxLayout)
        answerGroup = findViewById(R.id.answerGroup)
        confirmButton = findViewById(R.id.confirmButton)
        earnings = findViewById(R.id.earnings)
        gameDataHandler = GameDataHandler(this)

        displayQuestion(currQIndex)

        //sets background color for radio buttons upon selection
        answerGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedRBtn?.setBackgroundColor(Color.TRANSPARENT)
            selectedRBtn = findViewById(checkedId)
            selectedRBtn?.setBackgroundColor(Color.parseColor("#00FF00"))
        }

        confirmButton.setOnClickListener {
            //checks if radio button is selected
            var selected = -1

            if (answerGroup.visibility == View.VISIBLE) {
                // For single-answer questions
                selected = answerGroup.checkedRadioButtonId
            } else if (checkBoxesLayout.visibility == View.VISIBLE) {
                // For multiple-answer questions
                for (i in 0 until checkBoxesLayout.childCount) {
                    val view = checkBoxesLayout.getChildAt(i)
                    if (view is CheckBox && view.isChecked) {
                        selected = i + 1 // Set selected index (1-based) for multiple-answer questions
                        break
                    }
                }
            }

            //if an option is selected -> move on
            if (selected != -1) {
                val question = questions[currQIndex]
                val isMultipleAnswer = question.correctOptionIndices.size > 1

                AlertDialog.Builder(this)
                    .setTitle("Confirm Answer")
                    .setMessage("Are you sure you want to select this answer?")
                    .setPositiveButton("Confirm") { _, _ ->
                        //checks if selected radio button is correct option
                        checkAnswer(selected)
                        //checks if there is another question
                        //if true increments question index and displays next question
                        if (currQIndex < questions.size - 1) {
                            currQIndex++
                            displayQuestion(currQIndex)
                        } else {
                            //when there are no more questions -> move to stat screen
                            val intent = Intent(this@GameActivity, StatsActivity::class.java)
                            startActivity(intent)

                            //saves total earnings and amount of correct answers
                            val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()

                            val finalScore = runningTotal

                            editor.putInt("runningTotal", finalScore)
                            editor.putInt("finalCorrect", numCorrect)

                            editor.apply()

                            showToast("Game complete!")

                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            } else {
                showToast("Please select an option!")
            }
        }
    }

    //displays toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayQuestion(Index: Int) {
        //sets question based on provided index
        val question = questions[Index]
        //sets question text
        questionTextView.text = question.text

        val isMultipleAnswer = question.correctOptionIndices.size > 1

        if (isMultipleAnswer) {
            checkBoxesLayout.visibility = View.VISIBLE
            answerGroup.visibility = View.GONE
            displayMultipleAnswerQuestion(question)
        } else {
            answerGroup.visibility = View.VISIBLE
            checkBoxesLayout.visibility = View.GONE
            displaySingleAnswerQuestion(question)
        }
        //removes previous answer options
        answerGroup.removeAllViews()

        //sets new answer options
        for (i in question.options.indices) {
            val radioButton = RadioButton(this)
            radioButton.text = question.options[i]
            radioButton.id = i + 1
            answerGroup.addView(radioButton)
        }
        //unchecks previous selection
        answerGroup.clearCheck()
    }


    private fun checkAnswer(selected: Int) {
        val question = questions[currQIndex]

        //checks if selected radio button is equal to the correct option
        if (question.correctOptionIndices.contains(selected)) {
            //adds payout to total
            runningTotal += question.payout
            //increments number of correct answers
            numCorrect++
            //displays total
            earnings.text = "You Earned: $runningTotal Imperial Credits"
            showToast("Correct! You earned ${question.payout} credits!")
        } else {
            showToast("Wrong answer, rebel scum!")
        }
    }

    private fun displaySingleAnswerQuestion(question: GameActivity.Question) {
        answerGroup.removeAllViews()
        for (i in question.options.indices) {
            val radioButton = RadioButton(this)
            radioButton.text = question.options[i]
            radioButton.id = i + 1
            answerGroup.addView(radioButton)
        }
    }

    private fun displayMultipleAnswerQuestion(question: GameActivity.Question) {
        checkBoxesLayout.removeAllViews()
        for (i in question.options.indices) {
            val checkBox = CheckBox(this)
            checkBox.text = question.options[i]
            checkBox.id = i + 1
            checkBoxesLayout.addView(checkBox)
        }
    }

    private fun saveGameData() {
        gameDataHandler.saveGameData(runningTotal, numCorrect)
    }

    private fun loadGameHistory() {
        val (savedRunningTotal, savedNumCorrect) = gameDataHandler.loadGameHistory()

        runningTotal = savedRunningTotal
        numCorrect = savedNumCorrect
        earnings.text = "You Earned: $runningTotal Imperial Credits"
    }
}
