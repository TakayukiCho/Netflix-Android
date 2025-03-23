@file:OptIn(KoinExperimentalAPI::class)

package com.codandotv.streamplayerapp.feature_list_streams.list.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.codandotv.streamplayerapp.core_navigation.routes.BottomNavRoutes.HOME_COMPLETE
import com.codandotv.streamplayerapp.core_navigation.routes.BottomNavRoutes.PARAM.PROFILE_ID
import com.codandotv.streamplayerapp.core_navigation.routes.Routes
import com.codandotv.streamplayerapp.core_navigation.routes.Routes.DETAIL
import com.codandotv.streamplayerapp.core_navigation.routes.Routes.PROFILE_PICKER
import com.codandotv.streamplayerapp.feature_list_streams.list.di.ListStreamModule
import com.codandotv.streamplayerapp.feature_list_streams.list.presentation.screens.ListStreamsScreen
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.ksp.generated.module

internal const val DEFAULT_ID = ""

fun NavGraphBuilder.listStreamsNavGraph(navController: NavHostController) {
    composable(HOME_COMPLETE) { nav ->
        BackHandler(true) {}
        rememberKoinModules {
            listOf(ListStreamModule().module)
        }
        ListStreamsScreen(navController = navController,
            onNavigateDetailList = { id ->
                navController.navigate("${DETAIL}${id}")
            },
            onNavigateProfilePicker = {
                navController.navigate(PROFILE_PICKER)
            },
            onNavigateSearchScreen = {
                navController.navigate(Routes.SEARCH)
            },
            profilePicture = nav.arguments?.getString(PROFILE_ID) ?: DEFAULT_ID
        )
    }
}
