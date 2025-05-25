// app/src/main/java/com/ucb/ucbtest/navigation/AppNavigation.kt
package com.ucb.ucbtest.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucb.ucbtest.home.HomeUI
import com.ucb.ucbtest.simdelivery.SimDeliveryUI

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.HomeScreen.route) {
            HomeUI(
                onPlanSelected = { planName ->
                    // Navegar a la pantalla de SIM delivery cuando se selecciona un plan
                    navController.navigate(Screen.SimDeliveryScreen.route)
                }
            )
        }

        composable(Screen.SimDeliveryScreen.route) {
            SimDeliveryUI(
                onContinue = {
                    // Aquí podrías navegar a otra pantalla o mostrar confirmación
                    // Por ahora regresamos al inicio
                    navController.popBackStack()
                }
            )
        }
    }
}