package com.ucb.ucbtest.simdelivery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.domain.SimDelivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SimDeliveryViewModel @Inject constructor() : ViewModel() {

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private val _deliveryInfo = MutableStateFlow<SimDelivery?>(null)
    val deliveryInfo: StateFlow<SimDelivery?> = _deliveryInfo

    fun saveDeliveryInfo(
        referencePhone: String,
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                // Simular llamada a API
                delay(1500)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val currentTimestamp = dateFormat.format(Date())

                val delivery = SimDelivery(
                    referencePhone = referencePhone,
                    latitude = latitude,
                    longitude = longitude,
                    address = getAddressFromCoordinates(latitude, longitude),
                    timestamp = currentTimestamp
                )

                _deliveryInfo.value = delivery
                _uiState.value = UiState.Success

            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al guardar la información: ${e.message}")
            }
        }
    }

    private fun getAddressFromCoordinates(latitude: Double, longitude: Double): String {
        // Simular geocodificación inversa
        return when {
            latitude in -17.4..-17.3 && longitude in -66.2..-66.1 ->
                "Cochabamba, Bolivia"
            latitude in -16.6..-16.4 && longitude in -68.2..-68.0 ->
                "La Paz, Bolivia"
            latitude in -17.9..-17.7 && longitude in -63.2..-63.0 ->
                "Santa Cruz, Bolivia"
            else -> "Ubicación desconocida"
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }

    fun validatePhoneNumber(phone: String): Boolean {
        // Validación básica para números bolivianos
        val cleanPhone = phone.replace(Regex("[^0-9+]"), "")
        return cleanPhone.length >= 8 && (
                cleanPhone.startsWith("+591") ||
                        cleanPhone.startsWith("591") ||
                        cleanPhone.length in 8..9
                )
    }

    fun formatCoordinate(value: Double): String {
        return String.format("%.6f", value)
    }
}