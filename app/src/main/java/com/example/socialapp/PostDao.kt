package com.example.socialapp

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")

    fun addPost(post: Post){
        postCollection.document().set(post)
    }

    fun updateLikes(postId: String){
        GlobalScope.launch {
            val currentUserid = Firebase.auth.uid
            val post = getPostById(postId).await().toObject(Post::class.java)
            val isLiked = post!!.likedBy.contains(currentUserid)

            if(isLiked){
                post.likedBy.remove(currentUserid)
            } else {
                post.likedBy.add(currentUserid!!)
            }
            postCollection.document(postId).set(post)
        }
    }

    fun deletePost(postId: String){
        GlobalScope.launch {
            db.collection("posts").document(postId).delete()
        }
    }

    private fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollection.document(postId).get()
    }


}