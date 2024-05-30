package com.kryptopass.cinematix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.kryptopass.cinematix.ui.compose.movie.list.MovieListScreen
import com.kryptopass.cinematix.ui.compose.movie.single.MovieScreen
import com.kryptopass.cinematix.ui.compose.nav.LoginScreen
import com.kryptopass.cinematix.ui.compose.search.SearchScreen
import com.kryptopass.cinematix.ui.compose.nav.SignUpScreen
import com.kryptopass.common.nav.NavRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            App(navController)
        }
        FirebaseApp.initializeApp(this)
    }
}

@Composable
fun App(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomBarState = remember { mutableStateOf(true) }

    when (navBackStackEntry?.destination?.route) {
        NavRoutes.Movies.route,
        NavRoutes.Search.route -> {
            bottomBarState.value = true
        }

        else -> {
            bottomBarState.value = false
        }
    }

    Scaffold(
        // top bar
        bottomBar = {
            if (bottomBarState.value) {
                BottomAppBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = NavRoutes.Movies.route) {
                MovieListScreen(hiltViewModel(), navController)
            }
            composable(
                route = NavRoutes.Movie.route,
                arguments = NavRoutes.Movie.arguments
            ) {
                MovieScreen(hiltViewModel(), NavRoutes.Movie.fromEntry(it))
            }
            composable(route = NavRoutes.Search.route) {
                SearchScreen()
            }
            composable(route = NavRoutes.Login.route) {
                LoginScreen(navController)
            }
            composable(route = NavRoutes.SignUp.route) {
                SignUpScreen(navController)
            }
        }
    }
}

@Composable
fun BottomAppBar(navController: NavHostController) {
    var showSignOutDialog by remember { mutableStateOf(false) }

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = stringResource(R.string.home_desc)
                )
            },
            selected = navController.currentDestination?.route == NavRoutes.Movies.route,
            onClick = {
                navController.navigate(NavRoutes.Movies.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_desc)
                )
            },
            selected = navController.currentDestination?.route == NavRoutes.Search.route,
            onClick = {
                navController.navigate(NavRoutes.Search.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = stringResource(R.string.sign_out_desc)
                )
            },
            selected = false,
            onClick = { showSignOutDialog = true }
        )
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text(stringResource(R.string.sign_out_head)) },
            text = { Text(stringResource(R.string.sign_out_confirm)) },
            confirmButton = {
                OutlinedButton(
                    onClick = {
                        showSignOutDialog = false
                        navController.navigate(NavRoutes.Login.route) {
                            popUpTo(NavRoutes.Movies.route) { inclusive = true }
                        }

                    },
                    modifier = Modifier
                        .width(80.dp)
                        .height(48.dp)
                        .padding(vertical = 1.5.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(stringResource(R.string.yes_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text(
                        stringResource(R.string.no_button),
                        color = Color.Black,
                        modifier = Modifier
                            .height(48.dp)
                            .padding(vertical = 1.5.dp),
                    )
                }
            }
        )
    }
}