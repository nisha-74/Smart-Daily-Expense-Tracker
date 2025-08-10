package com.example.smartdailyexpensetracker.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdailyexpensetracker.domen.model.Expense
import com.example.smartdailyexpensetracker.domen.usecases.GetExpenseUseCase
import com.example.smartdailyexpensetracker.domen.usecases.GetTotalSpentTodayUseCase
import com.example.smartdailyexpensetracker.domen.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpenseUseCase,
    private val getTotalSpentTodayUseCase: GetTotalSpentTodayUseCase
) : ViewModel() {

    val expenseList = MutableLiveData<List<Expense>>(emptyList())
    val totalSpent = MutableLiveData(0.0)
    val totalCount = MutableLiveData(0)

    val selectedDateLabel = MutableLiveData("Today")
    val isGroupByCategory = MutableLiveData(true)

    private var currentStartDate: Long = 0
    private var currentEndDate: Long = 0

    init {
        loadTodayExpenses()
    }

    fun loadTodayExpenses() {
        val (start, end) = DateUtils.todayRange()
        currentStartDate = start
        currentEndDate = end
        selectedDateLabel.value = "Today"
        loadExpensesForRange(start, end)
    }

    fun onDateSelected(date: Long) {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val today = sdf.format(Date())
        val selected = sdf.format(Date(date))

        selectedDateLabel.value = if (selected == today) "Today" else selected

        val (start, end) = DateUtils.dateRange(date)
        currentStartDate = start
        currentEndDate = end
        loadExpensesForRange(start, end)
    }

    fun toggleGrouping(isCategory: Boolean) {
        isGroupByCategory.value = isCategory
        // TODO: Update list grouping in the adapter or transform data
    }

    private fun loadExpensesForRange(start: Long, end: Long) {
        viewModelScope.launch {
            getExpensesUseCase(start, end).collect { list ->
                expenseList.postValue(list)
                totalCount.postValue(list.size)
            }
        }
        viewModelScope.launch {
            getTotalSpentTodayUseCase(start, end).collect { total ->
                totalSpent.postValue(total)
            }
        }
    }
}
