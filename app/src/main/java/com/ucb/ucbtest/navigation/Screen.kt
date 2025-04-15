package com.ucb.ucbtest.navigation

sealed class Screen(val route: String) {
    object GitaliasScreen : Screen("gitlab")
    object TakePhotoScreen: Screen("takephoto")
    object MenuScreen: Screen("menu")
    object LoginScreen: Screen("login")
    object MoviesScreen: Screen("movies")
    object MovieDetailScreen: Screen("movieDetail")
    object CounterScreen: Screen("counter")
    object ExpenseScreen : Screen("expenses")
    object IncomeScreen : Screen("incomes")
    object FinanceScreen : Screen("finance")
    object SummaryScreen : Screen("summary")
    object BookSearchScreen : Screen("bookSearch")
    object BookDetailScreen : Screen("bookDetail")
}