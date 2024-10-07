package com.example.mommycat

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

class RegisterActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        database = AppDatabase.getDatabase(this)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextPhoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val buttonToLogin = findViewById<Button>(R.id.buttonToLogin)

        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val phoneNumber = editTextPhoneNumber.text.toString().trim()

            if (!validateInput(username, password, phoneNumber)) {
                Toast.makeText(this, "Mohon periksa kembali inputan Anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val isUnique = withContext(Dispatchers.IO) { isUsernameUnique(username) }
                if (!isUnique) {
                    Toast.makeText(this@RegisterActivity, "Username sudah digunakan", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                withContext(Dispatchers.IO) { addUser(username, password, phoneNumber) }
                Toast.makeText(this@RegisterActivity, "Pengguna berhasil didaftarkan", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        buttonToLogin.setOnClickListener {
            finish()
        }
    }

    private suspend fun isUsernameUnique(username: String): Boolean {
        return database.userDao().getUsernameCount(username) == 0
    }

    private suspend fun addUser(username: String, password: String, phoneNumber: String) {
        val user = User(
            id = UUID.randomUUID().toString(),
            username = username,
            password = password,
            phoneNumber = phoneNumber
        )
        database.userDao().insert(user)
    }

    private fun validateInput(username: String, password: String, phoneNumber: String): Boolean {
        val usernamePattern = "^[a-zA-Z0-9]{8,}$".toRegex()
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#\$%^&*]).{5,}$".toRegex()
        val phoneNumberPattern = "^[0+][0-9]{7,19}$".toRegex()

        return username.matches(usernamePattern) &&
                password.matches(passwordPattern) &&
                phoneNumber.matches(phoneNumberPattern)
    }
}
