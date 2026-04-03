package com.example.medicareplus.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicareplus.data.NotificationHistory
import com.example.medicareplus.databinding.ItemHistoryBinding
import java.util.Calendar

class HistoryAdapter : ListAdapter<NotificationHistory, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: NotificationHistory) {
            binding.tvMedicineName.text = history.medicineName
            binding.tvAction.text = history.action
            
            val calendar = Calendar.getInstance().apply { timeInMillis = history.time }
            binding.tvTime.text = DateFormat.format("dd MMM, hh:mm a", calendar)
            
            val color = when(history.action) {
                "Taken" -> 0xFF4CAF50.toInt()
                "Missed" -> 0xFFF44336.toInt()
                else -> 0xFFFF9800.toInt()
            }
            binding.tvAction.setTextColor(color)
        }
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<NotificationHistory>() {
        override fun areItemsTheSame(oldItem: NotificationHistory, newItem: NotificationHistory) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: NotificationHistory, newItem: NotificationHistory) = oldItem == newItem
    }
}
