package com.example.smartdailyexpensetracker.domen.model

import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class Expense(
    val id: Int = 0,
    val title: String,
    val amount: String,
    val category: String,
    val notes: String,
    val date: Long = System.currentTimeMillis()
){
    val displayDate: String
        get() {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return sdf.format(Date(date))
        }
}
