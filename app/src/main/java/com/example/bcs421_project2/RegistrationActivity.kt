package com.example.bcs421_project2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val editTextfName = findViewById<EditText>(R.id.editFName)
        val editTextlName = findViewById<EditText>(R.id.editLName)
        val editTextBirth = findViewById<EditText>(R.id.editBirth)
        val editTextEmail = findViewById<EditText>(R.id.editEmail)
        val editTextPassword = findViewById<EditText>(R.id.editPassword)
        val btn = findViewById<Button>(R.id.btn)

        btn.setOnClickListener {
            val fName = editTextfName.text.toString()
            val lName = editTextlName.text.toString()
            val birth = editTextBirth.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            clearErrorMessages()

            if (isValidRegistrationData(fName, lName, birth, email, password)) {
                val intent = Intent(this@RegistrationActivity, LoginOrRegisterActivity::class.java)
                startActivity(intent)

                val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                val newfName = fName
                val newlName = lName
                val newBirth = birth
                val newEmail = email
                val newPassword = password

                editor.putString("newfName", newfName)
                editor.putString("newlName", newlName)
                editor.putString("newBirth", newBirth)
                editor.putString("newEmail", newEmail)
                editor.putString("newPassword", newPassword)

                editor.apply()

                showToast("Registration successful!")
            }
            else {
                showToast("Registration unsuccessful! ):")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidRegistrationData(
        fName: String,
        lName: String,
        birth: String,
        email: String,
        password: String
    ): Boolean {
        val fNameError = findViewById<TextView>(R.id.fNameError)
        val lNameError = findViewById<TextView>(R.id.lNameError)
        val birthError = findViewById<TextView>(R.id.birthError)
        val emailError = findViewById<TextView>(R.id.emailError)
        val passwordError = findViewById<TextView>(R.id.passwordError)

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        var isValid = true

        if (fName.isEmpty()) {
            setErrorMessage(fNameError, "First name cannot be blank")
            isValid = false
        } else if (fName.length < 3 || fName.length > 30) {
            setErrorMessage(fNameError, "First name needs to be between 3 and 30 characters")
            isValid = false
        }
        if (lName.isEmpty()) {
            setErrorMessage(lNameError, "Last name cannot be blank")
            isValid = false
        }
        if (birth.isEmpty()) {
            setErrorMessage(birthError, "Birthdate cannot be blank")
            isValid = false
        }
        if (email.isEmpty()) {
            setErrorMessage(emailError, "Email cannot be blank")
            isValid = false
        } else if (!email.matches(emailPattern.toRegex())) {
            setErrorMessage(emailError, "Invalid email")
            isValid = false
        }
        if (password.isEmpty()) {
            setErrorMessage(passwordError, "Password cannot be blank")
            isValid = false
        } else if (password.length < 6) {
            setErrorMessage(passwordError, "Password must be at least 6 characters")
            isValid = false
        }
        return isValid
    }

    private fun setErrorMessage(textView: TextView, errorMessage: String) {
        textView.text = errorMessage
        textView.visibility = View.VISIBLE
    }

    private fun clearErrorMessages() {
        findViewById<TextView>(R.id.fNameError).visibility = View.GONE
        findViewById<TextView>(R.id.lNameError).visibility = View.GONE
        findViewById<TextView>(R.id.birthError).visibility = View.GONE
        findViewById<TextView>(R.id.emailError).visibility = View.GONE
        findViewById<TextView>(R.id.passwordError).visibility = View.GONE
    }
}