package com.ucb.domain

import kotlinx.serialization.Serializable

@Serializable
data class MobilePlan(
    val id: String,
    val name: String,
    val originalPrice: Double,
    val currentPrice: Double,
    val dataAmount: String,
    val features: List<String>,
    val isPopular: Boolean = false,
    val color: String = "#FF6B6B", // Color del plan
    val whatsappNumber: String = "59173799571" // Número para WhatsApp
)