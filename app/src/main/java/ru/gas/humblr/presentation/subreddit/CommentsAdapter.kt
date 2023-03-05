package ru.gas.humblr.presentation.subreddit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class CommentsAdapter(private val commentData: List<CommentData>) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(Comment(parent.context))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(commentData[position])
    }

    override fun getItemCount(): Int {
        return commentData.size
    }

    class ViewHolder(private val comment: Comment) :
        RecyclerView.ViewHolder(comment) {
        fun onBind(commentData: CommentData) {
            comment.setAvatar(commentData.avatar)
            comment.setName(commentData.name)
            comment.setComment(commentData.comment)
        }
    }
}