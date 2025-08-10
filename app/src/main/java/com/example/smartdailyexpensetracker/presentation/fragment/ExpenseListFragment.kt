package com.example.smartdailyexpensetracker.presentation.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartdailyexpensetracker.R
import com.example.smartdailyexpensetracker.databinding.FragmentExpenseListBinding
import com.example.smartdailyexpensetracker.presentation.adapter.ExpenseAdapter
import com.example.smartdailyexpensetracker.presentation.viewmodel.ExpenseListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
@AndroidEntryPoint
class ExpenseListFragment : Fragment() {
    private lateinit var binding: FragmentExpenseListBinding
    private val viewModel: ExpenseListViewModel by viewModels()
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_expense_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setupRecyclerView()
        setupListeners()

        return  binding.root
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter()
        binding.rvExpenses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = expenseAdapter
        }


        viewModel.expenseList.observe(viewLifecycleOwner) { expenses ->
            expenseAdapter.submitList(expenses)
        }


    }
    private fun setupListeners() {
        // Date picker
        binding.btnSelectDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedCal = Calendar.getInstance()
                    selectedCal.set(year, month, dayOfMonth)
                    viewModel.onDateSelected(selectedCal.timeInMillis)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        // Group toggle
        binding.toggleGroupBy.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleGrouping(isChecked)
        }

    }


}