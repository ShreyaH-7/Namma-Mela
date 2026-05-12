package com.nammamela.app.ui.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nammamela.app.databinding.ItemSeatBinding

class SeatAdapter(
    private val onSeatClicked: (String) -> Unit,
) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {

    private val seats = mutableListOf<String>()
    private val reservedSeats = mutableSetOf<String>()
    private val selectedSeats = mutableSetOf<String>()

    init {
        // 100 seats gives us a wide 20-column theatre-style layout with multiple rows.
        repeat(100) { index ->
            seats.add("S${index + 1}")
        }
        setHasStableIds(true)
    }

    fun updateReservedSeats(items: List<String>) {
        reservedSeats.clear()
        reservedSeats.addAll(items)
        selectedSeats.removeAll(items.toSet())
        notifyDataSetChanged()
    }

    fun getSelectedSeats(): List<String> = selectedSeats.toList()

    fun clearSelectedSeats() {
        if (selectedSeats.isEmpty()) return
        selectedSeats.clear()
        notifyDataSetChanged()
        onSeatClicked("")
    }

    override fun getItemId(position: Int): Long = seats[position].hashCode().toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val binding = ItemSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        holder.bind(seats[position])
    }

    override fun getItemCount(): Int = seats.size

    inner class SeatViewHolder(
        private val binding: ItemSeatBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(seatNumber: String) {
            binding.tvSeatNumber.text = seatNumber

            val isReserved = reservedSeats.contains(seatNumber)
            val isSelected = selectedSeats.contains(seatNumber)

            val background = when {
                isReserved -> com.nammamela.app.R.drawable.bg_seat_reserved
                isSelected -> com.nammamela.app.R.drawable.bg_seat_selected
                else -> com.nammamela.app.R.drawable.bg_seat_available
            }

            binding.root.setBackgroundResource(background)
            binding.root.alpha = if (isReserved) 0.72f else 1f
            binding.root.scaleX = if (isSelected) 1.08f else 1f
            binding.root.scaleY = if (isSelected) 1.08f else 1f
            binding.root.setOnClickListener {
                if (isReserved) return@setOnClickListener

                if (selectedSeats.contains(seatNumber)) {
                    selectedSeats.remove(seatNumber)
                } else {
                    selectedSeats.add(seatNumber)
                }

                notifyItemChanged(bindingAdapterPosition)
                onSeatClicked(
                    selectedSeats
                        .sortedBy { it.removePrefix("S").toIntOrNull() ?: Int.MAX_VALUE }
                        .joinToString(", ")
                )
            }
        }
    }
}
