package com.codandotv.streamplayerapp.core_navigation.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

/**
 * Composable that automatically tracks view events when navigation changes
 */
@Composable
fun NavigationAnalyticsTracker(navController: NavController) {
    val currentRoute = currentRoute(navController)
    
    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            AnalyticsHelper.trackViewEvent(route)
        }
    }
}