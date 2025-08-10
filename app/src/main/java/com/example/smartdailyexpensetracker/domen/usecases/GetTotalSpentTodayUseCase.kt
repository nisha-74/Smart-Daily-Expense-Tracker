package com.example.smartdailyexpensetracker.domen.usecases

import com.example.smartdailyexpensetracker.domen.repository.ExpanseRepo
import javax.inject.Inject

class GetTotalSpentTodayUseCase @Inject constructor(private val  repo: ExpanseRepo) {
    operator fun invoke(start:Long, end:Long)=repo.getTotalSpentByDate(start, end)
}