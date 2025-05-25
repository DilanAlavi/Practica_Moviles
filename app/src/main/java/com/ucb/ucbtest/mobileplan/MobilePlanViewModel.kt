
package com.ucb.ucbtest.mobileplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.domain.MobilePlan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MobilePlanViewModel @Inject constructor() : ViewModel() {

    private val _plans = MutableStateFlow<List<MobilePlan>>(emptyList())
    val plans: StateFlow<List<MobilePlan>> = _plans

    private val _currentPlanIndex = MutableStateFlow(0)
    val currentPlanIndex: StateFlow<Int> = _currentPlanIndex

    fun loadPlans() {
        viewModelScope.launch {
            val mockPlans = listOf(
                MobilePlan(
                    id = "flex5",
                    name = "Plan FLEX 5",
                    originalPrice = 270.0,
                    currentPrice = 199.0,
                    dataAmount = "5GB",
                    features = listOf(
                        "Llamadas y SMS ilimitados",
                        "Hotspot: Comparte tus datos",
                        "Redes Sociales ilimitadas incluidas",
                        "Arma tu plan con más apps ilimitadas",
                        "CO2 Negativo"
                    ),
                    isPopular = false,
                    color = "#FF6B6B"
                ),
                MobilePlan(
                    id = "flex8",
                    name = "Plan FLEX 8",
                    originalPrice = 370.0,
                    currentPrice = 299.0,
                    dataAmount = "8GB",
                    features = listOf(
                        "Llamadas y SMS ilimitados",
                        "Hotspot: Comparte tus datos",
                        "Redes Sociales ilimitadas incluidas",
                        "Arma tu plan con más apps ilimitadas",
                        "CO2 Negativo"
                    ),
                    isPopular = true,
                    color = "#FF6B6B"
                ),
                MobilePlan(
                    id = "flex10",
                    name = "Plan FLEX 10",
                    originalPrice = 470.0,
                    currentPrice = 399.0,
                    dataAmount = "10GB",
                    features = listOf(
                        "Llamadas y SMS ilimitados",
                        "Hotspot: Comparte tus datos",
                        "Redes Sociales ilimitadas incluidas",
                        "Arma tu plan con más apps ilimitadas",
                        "CO2 Negativo"
                    ),
                    isPopular = false,
                    color = "#4ECDC4"
                )
            )
            _plans.value = mockPlans
        }
    }

    fun showNextPlan() {
        val currentIndex = _currentPlanIndex.value
        val maxIndex = _plans.value.size - 1
        _currentPlanIndex.value = if (currentIndex < maxIndex) currentIndex + 1 else 0
    }

    fun showPreviousPlan() {
        val currentIndex = _currentPlanIndex.value
        val maxIndex = _plans.value.size - 1
        _currentPlanIndex.value = if (currentIndex > 0) currentIndex - 1 else maxIndex
    }

    fun getCurrentPlan(): MobilePlan? {
        val plans = _plans.value
        val index = _currentPlanIndex.value
        return if (plans.isNotEmpty() && index < plans.size) plans[index] else null
    }
}