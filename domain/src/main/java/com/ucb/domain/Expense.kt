package com.ucb.domain

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Expense(
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val description: String,
    val date: String
)