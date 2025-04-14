// app/src/main/java/com/ucb/ucbtest/finance/FinanceUI.kt
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
import com.ucb.ucbtest.expense.ExpenseUI
import com.ucb.ucbtest.income.IncomeUI
import com.ucb.ucbtest.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceUI() {
    val navController = rememberNavController()

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
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ExpenseScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.ExpenseScreen.route) {
                ExpenseUI()
            }
            composable(Screen.IncomeScreen.route) {
                IncomeUI()
            }
        }
    }
}