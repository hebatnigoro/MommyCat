package com.example.mommycat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = AppDatabase.getDatabase(this)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            lifecycleScope.launch {
                val isValidUser = validateUser(username, password)
                if (isValidUser) {
                    navigateToDashboard()
                } else {
                    Toast.makeText(this@MainActivity, "Invalid username or password.", Toast.LENGTH_LONG).show()
                }
            }
        }

        registerButton.setOnClickListener {
            navigateToRegister()
        }
    }

    private suspend fun validateUser(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            database.userDao().validateUser(username, password) > 0
        }
    }

    private fun saveLoggedInUserId(userId: UUID) {
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("LOGGED_IN_USER_ID", userId.toString())
            apply()
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this@MainActivity, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRegister() {
        val intent = Intent(this@MainActivity, RegisterActivity::class.java)
        startActivity(intent)
    }
}
