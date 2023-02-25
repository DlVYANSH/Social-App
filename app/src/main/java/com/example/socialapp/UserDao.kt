package com.example.socialapp

import com.google.firebase.firestore.FirebaseFirestore

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")

    fun addUser(user: User?){
        user?.let {
            userCollection.document(user.uid).set(it)
        }
    }
}