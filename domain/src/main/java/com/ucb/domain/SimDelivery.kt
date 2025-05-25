package com.ucb.domain

import kotlinx.serialization.Serializable

@Serializable
data class SimDelivery(
    val referencePhone: String,
    val latitude: Double,
    val longitude: Double,
    val address: String = "",
    val timestamp: String = ""
)