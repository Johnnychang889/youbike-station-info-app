package com.example.ubikeapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.ubikeapp.R
import com.example.ubikeapp.auth.AuthManager
import com.example.ubikeapp.auth.AuthManager.isEmailValid
import com.example.ubikeapp.auth.AuthManager.isPasswordValid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = AuthManager.getAuth()
        email = findViewById<EditText>(R.id.login_email)
        password = findViewById<EditText>(R.id.login_password)
        val login_GO = findViewById<Button>(R.id.login_GO)

        findViewById<TextView>(R.id.btn_signup).setOnClickListener {
            goSignupActivity()
        }

        login_GO.setOnClickListener {
            if (!isEmailValid(email.text.toString())) {
                Toast.makeText(this,"電子郵件格式錯誤",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isPasswordValid(password.text.toString())) {
                Toast.makeText(this,"密碼長度不足",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            login()
        }

    }

    private fun login() {
        auth.signInWithEmailAndPassword( email.text.toString(), password.text.toString() )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val currentUser = auth.currentUser
                    updateUI(currentUser)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("signInWithEmail:failure", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val isEmailVerified = user.isEmailVerified
                if (isEmailVerified == true) {
                    goMainActivity()
                }
                else {
                    Log.w("EmailVerified:failure", "用戶未驗證電子郵件", task.exception)
                    Toast.makeText(
                        baseContext,
                        "用戶未驗證電子郵件",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            } else {
                Log.w("reload:failure", "reload:failure", task.exception)
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun goMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun goSignupActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}