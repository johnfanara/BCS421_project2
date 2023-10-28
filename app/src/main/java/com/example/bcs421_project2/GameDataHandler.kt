package com.example.bcs421_project2

import android.content.Context

class GameDataHandler(context: Context) {
    private val PREFS_NAME = "GamePrefs"
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveGameData(runningTotal: Int, numCorrect: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("runningTotal", runningTotal)
        editor.putInt("numCorrect", numCorrect)
        editor.apply()
    }

    fun loadGameHistory(): Pair<Int, Int> {
        val runningTotal = sharedPreferences.getInt("runningTotal", 0)
        val numCorrect = sharedPreferences.getInt("numCorrect", 0)
        return Pair(runningTotal, numCorrect)
    }
}