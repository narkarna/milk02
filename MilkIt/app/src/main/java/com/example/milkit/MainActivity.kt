package com.example.milkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.milkit.presentation.cart.CartScreen
import com.example.milkit.presentation.checkout.CheckoutScreen
import com.example.milkit.presentation.home.HomeScreen
import com.example.milkit.presentation.products.ProductsScreen
import com.example.milkit.presentation.profile.ProfileScreen
import com.example.milkit.ui.theme.MilkItTheme
import com.example.milkit.util.Screen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // SystemClock.sleep(4000)
        setContent {
            MilkItTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        "MilkIt",
                                        color = colorResource(id = R.color.white)
                                    )
                                },
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = colorResource(
                                        id = R.color.main_theme
                                    )
                                )
                            )
                        },

                        bottomBar = {
                            BottomNavigation(navController = navController)
                        }
                    ) {

                        NavHost(
                            modifier = Modifier.padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding()),
                            navController = navController,
                            startDestination = Screen.HomeScreen.route
                        )
                        {
                            composable(route = Screen.HomeScreen.route) {
                                HomeScreen(navController)
                            }
                            composable(route = Screen.ProfileScreen.route) {
                                ProfileScreen()
                            }
                            composable(route = Screen.CartScreen.route,

                            ) {
                                CartScreen(navController)
                            }
                            composable(route = Screen.CheckoutScreen.route) {
                                //CheckoutScreen()
                            }
                            composable(route = Screen.ProductsScreen.route) {
                                ProductsScreen()
                            }
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Cart,
        BottomNavItem.Home,
        BottomNavItem.Profile
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                modifier = Modifier.background(color = colorResource(id = R.color.main_theme2)),
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 9.sp,
                        color = colorResource(id = R.color.white)
                    )
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.8f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {

    object Cart : BottomNavItem("Cart", R.drawable.cart_icon, "cart_screen")
    object Home : BottomNavItem("Home", R.drawable.home_icon, "home_screen")
    object Profile : BottomNavItem("Profile", R.drawable.profile_icon, "profile_screen")
}