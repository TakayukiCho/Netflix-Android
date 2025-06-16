@file:OptIn(KoinExperimentalAPI::class)

package com.codandotv.streamplayerapp.feature_list_streams.search.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.codandotv.streamplayerapp.core_navigation.routes.Routes
import com.codandotv.streamplayerapp.feature_list_streams.search.di.SearchModule
import com.codandotv.streamplayerapp.feature_list_streams.search.presentation.screens.SearchScreen
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

fun NavGraphBuilder.searchStreamsNavGraph(navController: NavHostController) {
    composable(Routes.SEARCH) { nav ->
        BackHandler(true) {}
         rememberKoinModules{
            listOf(SearchModule.module)
        }
        SearchScreen(
            navController = navController,
            onNavigateDetailList = { id ->
                navController.navigate("${Routes.DETAIL}${id}")
            },
        )
    }
}
