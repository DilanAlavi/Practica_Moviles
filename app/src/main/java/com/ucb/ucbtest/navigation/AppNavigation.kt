package com.ucb.ucbtest.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucb.ucbtest.book.BookUI
import com.ucb.ucbtest.book.BookDetailUI
import com.ucb.domain.Book

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.BookSearchScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.BookSearchScreen.route) {
            BookUI(
                onBookSelected = { book ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("book", book)
                    navController.navigate(Screen.BookDetailScreen.route)
                }
            )
        }

        composable(Screen.BookDetailScreen.route) {
            val book = navController.previousBackStackEntry?.savedStateHandle?.get<Book>("book")
            if (book != null) {
                BookDetailUI(
                    book = book,
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }
    }
}

