package com.example.smartdailyexpensetracker.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartdailyexpensetracker.data.local.ExpenseDB
import com.example.smartdailyexpensetracker.data.local.ExpenseDao
import com.example.smartdailyexpensetracker.data.repository.ExpenseRepoImp
import com.example.smartdailyexpensetracker.domen.repository.ExpanseRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun  provideDatabase(@ApplicationContext context: Context):ExpenseDB=
        Room.databaseBuilder(context,ExpenseDB::class.java , "ExpenseDb")
            .fallbackToDestructiveMigration().build()

    @Provides
    fun  provideExpenseDao(db: ExpenseDB)=db.expenseDao()

    @Provides
    fun  provideRepository(impl:ExpenseRepoImp):ExpanseRepo=impl



}