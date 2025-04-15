package com.ucb.ucbtest.finance

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucb.ucbtest.expense.ExpenseUI
import com.ucb.ucbtest.income.IncomeUI
import com.ucb.ucbtest.navigation.Screen
import com.ucb.ucbtest.summary.SummaryUI
import com.ucb.ucbtest.summary.SummaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceUI() {
    val navController = rememberNavController()

    // Crear un estado para saber cuando actualizar
    val needsRefresh = remember { mutableStateOf(false) }

    // Referencia al ViewModel de resumen para actualizarlo
    val summaryViewModel: SummaryViewModel = hiltViewModel()

    // Observar cambios en la navegación
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Cuando cambia la pestaña, disparamos la actualización si es necesario
    LaunchedEffect(currentRoute, needsRefresh.value) {
        if (needsRefresh.value) {
            // Si estamos en la pantalla de resumen, actualizamos los datos
            if (currentRoute == Screen.SummaryScreen.route) {
                summaryViewModel.refreshData()
            }
            // Restablecer el estado
            needsRefresh.value = false
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                NavigationBarItem(
                    icon = { Text("G") },
                    label = { Text("Gastos") },
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.ExpenseScreen.route } == true,
                    onClick = {
                        navController.navigate(Screen.ExpenseScreen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Text("I") },
                    label = { Text("Ingresos") },
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.IncomeScreen.route } == true,
                    onClick = {
                        navController.navigate(Screen.IncomeScreen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Text("R") },
                    label = { Text("Resumen") },
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.SummaryScreen.route } == true,
                    onClick = {
                        // Cuando se navega manualmente al resumen, forzamos la actualización
                        navController.navigate(Screen.SummaryScreen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        // Indicar que necesitamos refrescar los datos
                        needsRefresh.value = true
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.SummaryScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.ExpenseScreen.route) {
                ExpenseUI(
                    onNewExpense = {
                        // Cuando se añade o elimina un gasto, marcamos para refrescar
                        needsRefresh.value = true
                    }
                )
            }
            composable(Screen.IncomeScreen.route) {
                IncomeUI(
                    onNewIncome = {
                        // Cuando se añade o elimina un ingreso, marcamos para refrescar
                        needsRefresh.value = true
                    }
                )
            }
            composable(Screen.SummaryScreen.route) {
                SummaryUI(
                    refreshTrigger = needsRefresh.value
                )
            }
        }
    }
}