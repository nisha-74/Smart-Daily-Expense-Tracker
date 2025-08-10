package com.example.smartdailyexpensetracker.presentation.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartdailyexpensetracker.databinding.ItemCategoryTotalBinding
import com.example.smartdailyexpensetracker.presentation.viewmodel.CategoryTotal

class CategoryTotalsAdapter(private var items: List<CategoryTotal>) :
    RecyclerView.Adapter<CategoryTotalsAdapter.VH>() {

    inner class VH(val binding: ItemCategoryTotalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCategoryTotalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.binding.tvCategory.text = it.category
        holder.binding.tvCategoryTotal.text = "₹${String.format("%.2f", it.amount)}"
    }

    override fun getItemCount(): Int = items.size

    fun update(list: List<CategoryTotal>) {
        items = list
        notifyDataSetChanged()
    }
}
