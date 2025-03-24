@file:OptIn(KoinExperimentalAPI::class)

package com.codandotv.streamplayerapp.feature_profile.profile.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.codandotv.streamplayerapp.core_navigation.routes.BottomNavRoutes.HOME
import com.codandotv.streamplayerapp.core_navigation.routes.BottomNavRoutes.PARAM.PROFILE_ID
import com.codandotv.streamplayerapp.core_navigation.routes.Routes
import com.codandotv.streamplayerapp.feature_profile.profile.di.ProfilePickerStreamModule
import com.codandotv.streamplayerapp.feature_profile.profile.presentation.screens.ProfilePickerStreamScreen
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.ksp.generated.module

fun NavGraphBuilder.profilePickerStreamNavGraph(navController: NavHostController) {
    composable(Routes.PROFILE_PICKER) { nav ->
        rememberKoinModules {
            listOf(ProfilePickerStreamModule().module)
        }

        ProfilePickerStreamScreen(
            onNavigateListStreams = { profilePic ->
                navController.navigate("$HOME?$PROFILE_ID=$profilePic")
            }
        )
    }
}
