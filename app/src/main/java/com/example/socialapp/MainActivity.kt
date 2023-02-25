package com.example.socialapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), PostAdapter.ClickListener {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: PostAdapter
    private val postDao : PostDao = PostDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.floatingActionButton)
        recyclerView = findViewById(R.id.recyclerView)

        fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity :: class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val postCollection = postDao.postCollection
        //Show the items in descending order according to the time created
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        mAdapter = PostAdapter(recyclerViewOptions, this)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

    override fun onLikeClicked(id: String) {
        postDao.updateLikes(id)
    }

    override fun onDeleteBtnClicked(id: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete post?")
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { _, _ ->
                    postDao.deletePost(id)
                })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { _, _ ->

                })

        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.sign_out, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logOut -> showAlertDialog()
        }
        return true
    }

    private fun signOut() {
        Firebase.auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showAlertDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { _, _ ->
                    signOut()
                })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { _, _ ->

                })

        val alertDialog = builder.create()
        alertDialog.show()
    }
}