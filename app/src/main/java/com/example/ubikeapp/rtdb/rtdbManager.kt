package com.example.ubikeapp.rtdb

import com.example.ubikeapp.auth.AuthManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object rtdbManager {
    private val database = Firebase.database

    fun addData(path: String, data: Any, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val user = AuthManager.getCurrentUser()
        if (user == null) {
            onFailure("沒有權限")
            return
        }
        database.getReference(path)
            .child(user.uid)
            .push()
            .setValue(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun deleteData(path: String, key: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val user = AuthManager.getCurrentUser()
        if (user == null) {
            onFailure("沒有權限")
            return
        }
        database.getReference(path)
            .child(user.uid)
            .child(key)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun getData(path: String, onSuccess: (DataSnapshot) -> Unit, onFailure: (String) -> Unit) {
        val user = AuthManager.getCurrentUser()
        if (user == null) {
            onFailure("沒有權限")
            return
        }
        database.getReference(path)
            .child(user.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onSuccess(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }
            })
    }

    fun updateData(path: String, updates: Map<String, Any>, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        database.getReference(path)
            .updateChildren(updates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun findKeyForValue(path: String, value: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        val user = AuthManager.getCurrentUser()
        if (user == null) {
            onFailure("沒有權限")
            return
        }
        val query: Query = database.getReference(path).child(user.uid).orderByValue().equalTo(value)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val key: String = snapshot.key ?: ""
                    onSuccess(key)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onFailure("Unknown error")
            }
        })
    }
}





