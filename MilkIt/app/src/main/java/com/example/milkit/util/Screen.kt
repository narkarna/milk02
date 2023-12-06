package com.example.milkit.util

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object CartScreen: Screen("cart_screen")
    object CheckoutScreen: Screen("checkout_screen")
    object ProfileScreen: Screen("profile_screen")
    object ProductsScreen: Screen("product-screen")
}