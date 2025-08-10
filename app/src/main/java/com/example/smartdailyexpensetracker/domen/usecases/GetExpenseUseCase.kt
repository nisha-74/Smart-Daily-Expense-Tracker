package com.example.smartdailyexpensetracker.domen.usecases

import com.example.smartdailyexpensetracker.domen.repository.ExpanseRepo
import javax.inject.Inject

class GetExpenseUseCase @Inject constructor(private val repo:ExpanseRepo) {
    operator fun invoke(start:Long, end:Long)= repo.getExpensesByDate(start,end)
}