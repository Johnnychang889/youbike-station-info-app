package com.example.ubikeapp.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object AuthManager {
    private var auth: FirebaseAuth = Firebase.auth

    fun getAuth(): FirebaseAuth {
        return auth
    }
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    fun signOut() {
        auth.signOut()
    }
    fun isEmailValid(email: String): Boolean {
        val regexPattern = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        return regexPattern.matches(email)
    }
    fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}