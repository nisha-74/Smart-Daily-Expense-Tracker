package com.example.smartdailyexpensetracker.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartdailyexpensetracker.databinding.ItemDailyTotalBinding
import com.example.smartdailyexpensetracker.presentation.viewmodel.DailyTotal

class DailyTotalsAdapter(private var items: List<DailyTotal>) :
    RecyclerView.Adapter<DailyTotalsAdapter.VH>() {

    inner class VH(val binding: ItemDailyTotalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemDailyTotalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.binding.tvDay.text = it.dateLabel
        holder.binding.tvDayTotal.text = "₹${String.format("%.2f", it.amount)}"
    }

    override fun getItemCount(): Int = items.size

    fun update(list: List<DailyTotal>) {
        items = list
        notifyDataSetChanged()
    }
}
