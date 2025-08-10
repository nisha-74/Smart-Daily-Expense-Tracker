//package com.example.smartdailyexpensetracker.presentation.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.example.smartdailyexpensetracker.R
//import com.example.smartdailyexpensetracker.databinding.ItemExpenseBinding
//import com.example.smartdailyexpensetracker.domen.model.Expense
//
//class ExpenseAdapter : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val binding: ItemExpenseBinding =
//            DataBindingUtil.inflate(layoutInflater, R.layout.item_expense, parent, false)
//        return ExpenseViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    class ExpenseViewHolder(private val binding: ItemExpenseBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(expense: Expense) {
//            binding.expense = expense
//            binding.executePendingBindings()
//        }
//    }
//
//    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
//        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
//        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
//    }
//}
package com.example.smartdailyexpensetracker.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartdailyexpensetracker.R
import com.example.smartdailyexpensetracker.databinding.ItemExpenseBinding
import com.example.smartdailyexpensetracker.domen.model.Expense

class ExpenseAdapter :
    ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemExpenseBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_expense,
            parent,
            false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseViewHolder(private val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense) {
            binding.expense = expense
            binding.executePendingBindings()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}
