package com.example.smartdailyexpensetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smartdailyexpensetracker.domen.model.Expense

@Entity(tableName = "expense")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val  id:Int,
    val title:String,
                   val amount:String,
                   val category:String,
                   val notes:String,
                   val date:Long =System.currentTimeMillis())
fun Expense.toEntity()=
    ExpenseEntity(
    id = id,
        title = title, amount = amount, category = category, notes = notes,
        date = date,

    )

fun  ExpenseEntity.toDomain():Expense=Expense(
    id =id, title = title, amount =amount, category = category,
    notes = notes, date = date)