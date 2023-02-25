package com.example.socialapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(
    options: FirestoreRecyclerOptions<Post>,
    private val listener: ClickListener
    )
    : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(
    options
) {
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivUserImage: ImageView = itemView.findViewById(R.id.ivUserImage)
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)
        val tvPostTitle: TextView = itemView.findViewById(R.id.tvPostTitle)
        val ivLikeBtn: ImageView = itemView.findViewById(R.id.ivLikeBtn)
        val tvLikesCount: TextView = itemView.findViewById(R.id.tvLikesCount)
        val tvDeleteDialog: TextView = itemView.findViewById(R.id.tvDeleteDialog)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))

        viewHolder.ivLikeBtn.setOnClickListener {
            listener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.tvDeleteDialog.setOnClickListener {
            listener.onDeleteBtnClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.apply {
            Glide.with(ivUserImage.context).load(model.createdBy.imageUrl).circleCrop().into(ivUserImage)
            tvUserName.text = model.createdBy.displayName
            tvCreatedAt.text = Utils().getTimeAgo(model.createdAt)
            tvPostTitle.text = model.text
            tvLikesCount.text = model.likedBy.size.toString()

            val currentUserId = Firebase.auth.uid
            if(model.likedBy.contains(currentUserId)){
                ivLikeBtn.setImageResource(R.drawable.ic_liked)
            } else {
                ivLikeBtn.setImageResource(R.drawable.ic_unliked)
            }
        }
    }

    interface ClickListener{
        fun onLikeClicked(id: String)
        fun onDeleteBtnClicked(id: String)
    }
}