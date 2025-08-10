package com.example.smartdailyexpensetracker.presentation.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdailyexpensetracker.domen.model.Expense
import com.example.smartdailyexpensetracker.domen.usecases.AddExpenseUseCase
import com.example.smartdailyexpensetracker.domen.usecases.GetTotalSpentTodayUseCase
import com.example.smartdailyexpensetracker.domen.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class ExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val getTotalSpentTodayUseCase: GetTotalSpentTodayUseCase
) : ViewModel() {

    val title = MutableLiveData("")
    val amount = MutableLiveData("")
    val category = MutableLiveData("")
    val notes = MutableLiveData("")
    val totalSpentToday = MutableLiveData(0.0)
    val message = MutableLiveData<Boolean>()

    fun addExpense() {
        val titleText = title.value?.trim() ?: ""
        val amountValue = amount.value?.toDoubleOrNull() ?: 0.0

        if (titleText.isEmpty() || amountValue <= 0) {
            return
        }

        viewModelScope.launch {
            notes.value?.trim()?.let {
                Expense(
                    title = titleText,
                    amount = amountValue.toString(),
                    category = category.value ?: "Other",
                    notes = it
                )
            }?.let {
                addExpenseUseCase(
                    it
                )
            }
            message.postValue(true) // ✅ Notify activity
        }
    }

    fun loadTotalSpentToday() {
        val (start, end) = DateUtils.todayRange()
        viewModelScope.launch {
            getTotalSpentTodayUseCase(start, end).collect {
                totalSpentToday.postValue(it)
            }
        }
    }
    fun clearFields() {
        title.value = ""
        amount.value = ""
        category.value = ""
        notes.value = ""
    }

}

