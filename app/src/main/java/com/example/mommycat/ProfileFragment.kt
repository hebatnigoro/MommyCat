package com.example.mommycat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var database: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val textViewUsername: TextView = view.findViewById(R.id.textViewUsername)
        val textViewPhoneNumber: TextView = view.findViewById(R.id.textViewPhoneNumber)
        val buttonLogout: Button = view.findViewById(R.id.buttonLogout)
        val buttonAboutUs: Button = view.findViewById(R.id.buttonAboutUs)

        database = AppDatabase.getDatabase(requireActivity())

        lifecycleScope.launch {
            val currentUser = getUser()
            if (currentUser != null) {
                textViewUsername.text = currentUser.username
                textViewPhoneNumber.text = currentUser.phoneNumber
            } else {
                Log.d("ProfileFragment", "No user found")
            }
        }

        buttonLogout.setOnClickListener {
            logout()
        }

        buttonAboutUs.setOnClickListener {
            val intent = Intent(activity, AboutUsActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private suspend fun getUser(): User? {
        val sharedPref = requireActivity().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userIdString = sharedPref.getString("LOGGED_IN_USER_ID", null)
        Log.d("ProfileFragment", "User ID retrieved from shared preferences: $userIdString")

        val userId = userIdString?.let { UUID.fromString(it) } ?: return null

        return withContext(Dispatchers.IO) {
            val user = database.userDao().getUserById(userId)
            Log.d("ProfileFragment", "User retrieved from database: $user")
            user
        }
    }

    private fun logout() {
        val sharedPref = requireActivity().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            remove("LOGGED_IN_USER_ID")
            apply()
        }
        redirectToLoginPage()
    }

    private fun redirectToLoginPage() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
