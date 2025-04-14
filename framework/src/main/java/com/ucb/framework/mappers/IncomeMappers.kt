package com.ucb.framework.mappers

import com.ucb.domain.Income
import com.ucb.framework.persistence.IncomeEntity

fun Income.toEntity(): IncomeEntity {
    return IncomeEntity(
        id = id,
        name = name,
        amount = amount,
        description = description,
        date = date
    )
}

fun IncomeEntity.toDomain(): Income {
    return Income(
        id = id,
        name = name,
        amount = amount,
        description = description,
        date = date
    )
}