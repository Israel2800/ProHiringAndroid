package com.israelaguilar.prohiringandroid.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.databinding.ActivityCreateAccountBinding

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        setupListeners()
    }

    private fun setupListeners() {
        binding.createAccountBtn.setOnClickListener {
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()

            if (!isValidEmail(email) || !isValidPassword(password)) {
                Toast.makeText(this, getString(R.string.invalid_email_password), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserToFirestore(email)
                        Toast.makeText(this, getString(R.string.account_created_successfully), Toast.LENGTH_SHORT).show()
                        navigateToLogin()
                    } else {
                        Toast.makeText(this, getString(R.string.error_creating_account, task.exception?.message), Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.signInBtn.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@$!%*?&#]{8,}$"
        return password.matches(regex.toRegex())
    }

    private fun saveUserToFirestore(email: String) {
        val firestore = FirebaseFirestore.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val user = hashMapOf(
                "email" to email,
                "created_at" to FieldValue.serverTimestamp()
            )

            firestore.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, getString(R.string.user_saved_firestore), Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, getString(R.string.error_saving_user, e.message), Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, getString(R.string.user_not_authenticated), Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
