package com.example.ubikeapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ubikeapp.R
import com.example.ubikeapp.auth.AuthManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = AuthManager.getAuth()
        email = findViewById<EditText>(R.id.signup_email)
        password = findViewById<EditText>(R.id.signup_password)
        val signup_GO = findViewById<TextView>(R.id.signup_GO)

        findViewById<TextView>(R.id.btn_back).setOnClickListener {
            goLoginActivity()
        }

        signup_GO.setOnClickListener {
            if (!AuthManager.isEmailValid(email.text.toString())) {
                Toast.makeText(this,"電子郵件格式錯誤",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!AuthManager.isPasswordValid(password.text.toString())) {
                Toast.makeText(this,"密碼長度不足",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createUser()
        }

    }

    private fun createUser() {
        auth.createUserWithEmailAndPassword( email.text.toString(), password.text.toString() )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("createUserWithEmail:success", "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(
                        "createUserWithEmail:failure",
                        "createUserWithEmail:failure",
                        task.exception
                    )
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
        user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                if (verificationTask.isSuccessful) {
                    println("驗證電子郵件已發送至您的註冊郵箱")
                    Toast.makeText(
                        baseContext,
                        "驗證電子郵件已發送至您的註冊郵箱",
                        Toast.LENGTH_SHORT,
                    ).show()
                    goLoginActivity()
                } else {
                    println("驗證電子郵件發送失敗")
                    Toast.makeText(
                        baseContext,
                        "驗證電子郵件發送失敗",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

    }

    private fun goLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}