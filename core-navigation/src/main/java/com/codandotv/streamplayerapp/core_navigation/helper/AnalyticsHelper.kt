package com.codandotv.streamplayerapp.core_navigation.helper

import io.karte.android.tracking.Tracker

/**
 * KARTE SDK view events helper for navigation tracking
 */
object AnalyticsHelper {
    
    /**
     * Send view event based on route
     */
    fun trackViewEvent(route: String, movieId: String? = null, profileId: String? = null) {
        when {
            route.startsWith("splash") -> {
                Tracker.view("splash", "Splash Screen")
            }
            route.startsWith("bottomHome") -> {
                Tracker.view("home", "Home Screen")
            }
            route.startsWith("DetailList/") -> {
                val id = movieId ?: route.substringAfter("DetailList/")
                Tracker.view("stream_detail", "Stream Detail Screen", mapOf("movie_id" to id))
            }
            route.startsWith("Search") -> {
                Tracker.view("search", "Search Screen")
            }
            route.startsWith("profilePicker") -> {
                Tracker.view("profile_picker", "Profile Picker Screen")
            }
            route.startsWith("bottomGames") -> {
                Tracker.view("games", "Games Screen")
            }
            route.startsWith("bottomNews") -> {
                Tracker.view("news", "News Screen")
            }
            route.startsWith("bottomScenes") -> {
                Tracker.view("scenes", "Scenes Screen")
            }
            route.startsWith("bottomDownloads") -> {
                Tracker.view("downloads", "Downloads Screen")
            }
            else -> {
                // Fallback for unknown routes
                Tracker.view("unknown", "Unknown Screen", mapOf("route" to route))
            }
        }
    }
    
    /**
     * Get view name from route for consistent naming
     */
    fun getViewNameFromRoute(route: String): String {
        return when {
            route.startsWith("splash") -> "splash"
            route.startsWith("bottomHome") -> "home"
            route.startsWith("DetailList/") -> "stream_detail"
            route.startsWith("Search") -> "search"
            route.startsWith("profilePicker") -> "profile_picker"
            route.startsWith("bottomGames") -> "games"
            route.startsWith("bottomNews") -> "news"
            route.startsWith("bottomScenes") -> "scenes"
            route.startsWith("bottomDownloads") -> "downloads"
            else -> "unknown"
        }
    }
    
    /**
     * Get display title from route
     */
    fun getTitleFromRoute(route: String): String {
        return when {
            route.startsWith("splash") -> "Splash Screen"
            route.startsWith("bottomHome") -> "Home Screen"
            route.startsWith("DetailList/") -> "Stream Detail Screen"
            route.startsWith("Search") -> "Search Screen"
            route.startsWith("profilePicker") -> "Profile Picker Screen"
            route.startsWith("bottomGames") -> "Games Screen"
            route.startsWith("bottomNews") -> "News Screen"
            route.startsWith("bottomScenes") -> "Scenes Screen"
            route.startsWith("bottomDownloads") -> "Downloads Screen"
            else -> "Unknown Screen"
        }
    }
}