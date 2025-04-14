package com.ucb.framework.mappers

import com.ucb.domain.Expense
import com.ucb.framework.persistence.ExpenseEntity

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        name = name,
        amount = amount,
        description = description,
        date = date
    )
}

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        name = name,
        amount = amount,
        description = description,
        date = date
    )
}