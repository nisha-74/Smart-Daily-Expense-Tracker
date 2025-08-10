package com.example.smartdailyexpensetracker.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.smartdailyexpensetracker.R
import com.example.smartdailyexpensetracker.databinding.FragmentExpenseEntryBinding
import com.example.smartdailyexpensetracker.presentation.viewmodel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class ExpenseEntryFragment : Fragment() {
    private lateinit var binding: FragmentExpenseEntryBinding
    private val viewModel: ExpenseViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= DataBindingUtil.inflate(inflater,R.layout.fragment_expense_entry, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val categories = listOf("Staff", "Travel", "Food", "Utility")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_dropdown, categories)
        binding.categoryDropdown.setAdapter(adapter)


        // Load total spent today when screen opens
        viewModel.loadTotalSpentToday()

        // Observe a "saved" event from ViewModel
        // Observe save message from ViewModel
        viewModel.message.observe(viewLifecycleOwner) { saved ->
            if (saved == true) {
                Toast.makeText(requireContext(), "Expense added successfully!", Toast.LENGTH_SHORT).show()

            }
        }

        return  binding.root
    }


}