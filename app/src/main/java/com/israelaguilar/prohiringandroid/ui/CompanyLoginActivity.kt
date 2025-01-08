package com.israelaguilar.prohiringandroid.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseUser
import com.israelaguilar.prohiringandroid.R
import com.google.android.material.button.MaterialButton


class CompanyLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var accountField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordButton: Button
    private lateinit var createNewAccountButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSignInUser: Button


    //private val RC_SIGN_IN = 9001 // For Google sign-in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_login)

        auth = FirebaseAuth.getInstance()
        accountField = findViewById(R.id.accountFieldCL)
        passwordField = findViewById(R.id.passwordFieldCL)
        loginButton = findViewById(R.id.loginButtonCL)
        forgotPasswordButton = findViewById(R.id.forgotPasswordCL)
        createNewAccountButton = findViewById(R.id.createNewAccountCL)
        progressBar = findViewById(R.id.progressBarCL)
        btnSignInUser = findViewById(R.id.btnSignInUser)

        loginButton.setOnClickListener { loginTapped() }
        forgotPasswordButton.setOnClickListener { forgotPasswordTapped() }
        createNewAccountButton.setOnClickListener { createAccountTapped() }
        btnSignInUser.setOnClickListener { signInUserTapped() } // Configurar el OnClickListener


        // Check if already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            checkUserTypeAndNavigate(currentUser)
        }
    }

    private fun signInUserTapped() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    private fun loginTapped() {
        val email = accountField.text.toString()
        val password = passwordField.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Por favor, ingresa un correo y contraseña válidos.")
            return
        }

        showProgress(true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showProgress(false)
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let { checkUserTypeAndNavigate(it) }
                } else {
                    showMessage("Error al iniciar sesión: ${task.exception?.localizedMessage}")
                }
            }
    }

    private fun checkUserTypeAndNavigate(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()
        db.collection("companies").document(user.uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.exists()) {
                    // Company user
                    storeSession(user.uid, "company")
                    navigateToCompanyHome()
                } else {
                    db.collection("users").document(user.uid).get()
                        .addOnCompleteListener { userTask ->
                            if (userTask.isSuccessful && userTask.result.exists()) {
                                // Regular user
                                storeSession(user.uid, "user")
                                navigateToUserHome()
                            } else {
                                showMessage("Usuario no encontrado. Verifica tu cuenta.")
                            }
                        }
                }
            }
    }

    private fun storeSession(userUID: String, userType: String) {
        val sharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("loggedInUserUID", userUID)
        editor.putString("loggedInUserType", userType)
        editor.apply()
    }

    private fun navigateToCompanyHome() {
        val intent = Intent(this, CompanyProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToUserHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun forgotPasswordTapped() {
        val email = accountField.text.toString()
        if (email.isEmpty()) {
            showMessage("Por favor, ingresa un correo válido para recuperar la contraseña.")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showMessage("Correo de recuperación enviado. Revisa tu bandeja de entrada.")
                } else {
                    showMessage("Error al enviar el correo de recuperación: ${task.exception?.localizedMessage}")
                }
            }
    }

    private fun createAccountTapped() {
        val intent = Intent(this, CreateCompanyAccountActivity::class.java)
        startActivity(intent)
    }

    /*
    // Google sign-in
    private fun signInWithGoogleTapped() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                showMessage("Error al iniciar sesión con Google: ${e.localizedMessage}")
            }
        }
    }
     */
    /*
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let { checkUserTypeAndNavigate(it) }
                } else {
                    showMessage("Error al autenticar con Firebase: ${task.exception?.localizedMessage}")
                }
            }
    }
    */
}
