package com.nammamela.app.ui.fanwall

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nammamela.app.data.model.Comment
import com.nammamela.app.databinding.ItemCommentBinding

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private val items = mutableListOf<Comment>()

    fun submitList(comments: List<Comment>) {
        items.clear()
        items.addAll(comments)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class CommentViewHolder(
        private val binding: ItemCommentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.tvName.text = comment.name
            binding.tvMessage.text = comment.message
            binding.tvTime.text = comment.time.ifBlank { binding.root.context.getString(com.nammamela.app.R.string.just_now) }
            binding.root.contentDescription = "${comment.name}: ${comment.message}"
        }
    }
}
