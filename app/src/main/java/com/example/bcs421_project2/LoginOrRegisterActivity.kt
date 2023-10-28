package com.example.bcs421_project2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginOrRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_or_register)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        //moves to login screen
        loginBtn.setOnClickListener{
            val intent = Intent(this@LoginOrRegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        //moves to registration screen
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener {
            val intent = Intent(this@LoginOrRegisterActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}