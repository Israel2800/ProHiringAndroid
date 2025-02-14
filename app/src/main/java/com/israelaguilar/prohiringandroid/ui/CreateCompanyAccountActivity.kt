package com.israelaguilar.prohiringandroid.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.israelaguilar.prohiringandroid.R
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class CreateCompanyAccountActivity : AppCompatActivity() {

    private lateinit var logoImageView: ImageView
    private lateinit var emailField: TextInputEditText
    private lateinit var passwordField: TextInputEditText
    private lateinit var companyNameField: TextInputEditText
    private lateinit var servicesField: TextInputEditText
    private lateinit var socialMediaField: TextInputEditText
    private lateinit var contactField: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var signInBtn: TextView

    private val PICK_IMAGE_REQUEST = 71
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_company_account)

        logoImageView = findViewById(R.id.logoImageViewC)
        emailField = findViewById(R.id.emailFieldC)
        passwordField = findViewById(R.id.passwordFieldC)
        companyNameField = findViewById(R.id.companyNameFieldC)
        servicesField = findViewById(R.id.servicesFieldC)
        socialMediaField = findViewById(R.id.socialMediaFieldC)
        contactField = findViewById(R.id.contactFieldC)
        progressBar = findViewById(R.id.activityIndicator)
        signInBtn = findViewById(R.id.signInBtn)

        auth = FirebaseAuth.getInstance()

        signInBtn.setOnClickListener { signInBtn() }

        findViewById<Button>(R.id.selectLogoButton).setOnClickListener {
            openImageChooser()
        }

        findViewById<Button>(R.id.createAccountButton).setOnClickListener {
            createAccount()
        }
    }

    private fun openImageChooser() {
        if (hasReadExternalStoragePermission()) {
            launchImagePicker()
        } else {
            requestReadExternalStoragePermission()
        }
    }

    private fun hasReadExternalStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun requestReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast(getString(R.string.permission_required))
            }
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST
            )
        }
    }

    private fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast(getString(R.string.permission_granted))
                launchImagePicker()
            } else {
                showToast(getString(R.string.permission_denied))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            Picasso.get().load(imageUri).into(logoImageView)
        }
    }

    private fun createAccount() {
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()
        val companyName = companyNameField.text.toString().trim()
        val services = servicesField.text.toString().trim()
        val socialMedia = socialMediaField.text.toString().trim()
        val contact = contactField.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || companyName.isEmpty() || services.isEmpty() || socialMedia.isEmpty() || contact.isEmpty() || imageUri == null) {
            showToast(getString(R.string.fill_all_fields))
            return
        }

        progressBar.visibility = ProgressBar.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                progressBar.visibility = ProgressBar.INVISIBLE
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        uploadLogoToStorage(it.uid, imageUri!!) { logoUrl ->
                            if (logoUrl != null) {
                                saveCompanyDataToFirestore(it.uid, logoUrl, companyName, services, socialMedia, contact)
                            }
                        }
                    }
                } else {
                    showToast(getString(R.string.error_creating_account, task.exception?.message))
                }
            })
    }

    private fun uploadLogoToStorage(userId: String, imageUri: Uri, callback: (String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("logos/$userId.jpg")
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()

        storageRef.putBytes(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    callback(uri.toString())
                }
            } else {
                callback(null)
            }
        }
    }

    private fun saveCompanyDataToFirestore(userId: String, logoUrl: String, companyName: String, services: String, socialMedia: String, contact: String) {
        val db = FirebaseFirestore.getInstance()
        val companyData = hashMapOf(
            "logoURL" to logoUrl,
            "companyName" to companyName,
            "name" to emailField.text.toString(),
            "services" to services,
            "socialMedia" to socialMedia,
            "contact" to contact
        )

        db.collection("companies").document(userId).set(companyData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(getString(R.string.company_account_created))
                    navigateToProfile(userId)
                } else {
                    showToast(getString(R.string.error_saving_company_data, task.exception?.message))
                }
            }
    }

    private fun navigateToProfile(companyId: String) {
        val intent = Intent(this, CompanyProfileActivity::class.java)
        intent.putExtra("COMPANY_ID", companyId)
        startActivity(intent)
        finish()  // Close the account creation activity
    }

    private fun signInBtn(){
        val intent = Intent(this, CompanyLoginActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
