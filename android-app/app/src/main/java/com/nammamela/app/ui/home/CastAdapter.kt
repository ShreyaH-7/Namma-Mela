package com.nammamela.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nammamela.app.data.model.CastMember
import com.nammamela.app.databinding.ItemCastBinding

class CastAdapter : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private val items = mutableListOf<CastMember>()

    fun submitList(cast: List<CastMember>) {
        items.clear()
        items.addAll(cast)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class CastViewHolder(
        private val binding: ItemCastBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CastMember) {
            binding.ivCast.load(item.image)
            binding.tvCastName.text = item.name
            binding.tvCastRole.text = item.role
            binding.tvCastBio.text = item.bio
        }
    }
}
