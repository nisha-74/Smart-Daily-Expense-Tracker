package com.example.smartdailyexpensetracker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdailyexpensetracker.domen.usecases.GetExpenseUseCase
import com.example.smartdailyexpensetracker.domen.usecases.GetTotalSpentTodayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
data class DailyTotal(val dateLabel: String, val amount: Double)
data class CategoryTotal(val category: String, val amount: Double)


@HiltViewModel
class ExpenseReportViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpenseUseCase,

) : ViewModel() {

    private val _dailyTotals = MutableLiveData<List<DailyTotal>>()
    val dailyTotals: LiveData<List<DailyTotal>> = _dailyTotals

    private val _categoryTotals = MutableLiveData<List<CategoryTotal>>()
    val categoryTotals: LiveData<List<CategoryTotal>> = _categoryTotals

    init {
        loadLast7DaysData()
    }

    private fun loadLast7DaysData() {
        val cal = Calendar.getInstance()
        val endDate = cal.timeInMillis

        cal.add(Calendar.DAY_OF_YEAR, -6) // 7 days range
        val startDate = cal.timeInMillis

        viewModelScope.launch {
            getExpensesUseCase(startDate, endDate).collect { expenses ->
                val sdf = SimpleDateFormat("EEE, d MMM", Locale.getDefault())

                // ----- Daily totals -----
                val dailyMap = expenses.groupBy {
                    val dayCal = Calendar.getInstance()
                    dayCal.timeInMillis = it.date ?: System.currentTimeMillis()
                    sdf.format(dayCal.time)
                }.map { (day, list) ->
                    DailyTotal(day, list.sumOf { exp -> exp.amount.toDoubleOrNull() ?: 0.0 })
                }.sortedBy { day -> sdf.parse(day.dateLabel) }

                _dailyTotals.postValue(dailyMap)

                // ----- Category totals -----
                val categoryMap = expenses.groupBy { it.category ?: "Other" }
                    .map { (category, list) ->
                        CategoryTotal(category, list.sumOf { exp -> exp.amount.toDoubleOrNull() ?: 0.0 })
                    }

                _categoryTotals.postValue(categoryMap)
            }
        }
    }
}

