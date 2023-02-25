package com.example.socialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreatePostActivity : AppCompatActivity() {

    private lateinit var btnPost : Button
    private lateinit var etPost: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        etPost = findViewById(R.id.etCreatePost)
        btnPost = findViewById(R.id.btn_post)

        btnPost.setOnClickListener {
            val input: String = etPost.editText?.text.toString()
            if (input.isNotEmpty()) {
                val auth = Firebase.auth
                val currentUser = auth.currentUser
                val user = User(currentUser!!.uid, currentUser?.displayName, currentUser?.photoUrl.toString())
                val post = Post(input, user, System.currentTimeMillis())
                val postDao = PostDao()
                postDao.addPost(post)
                finish()
            }
        }
    }
}