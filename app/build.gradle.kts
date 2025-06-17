@file:Suppress("UnstableApiUsage")

plugins {
    id("com.streamplayer.application")
}

android {
    defaultConfig {
        buildConfigField("String", "KARTE_APP_KEY", "\"${property("KARTE_APP_KEY")}\"")
    }
}

dependencies {
    implementation(projects.featureFavorites)
    implementation(projects.featureListStreams)
    implementation(projects.featureProfile)
    implementation(projects.coreShared)
    implementation(projects.coreSharedUi)
    implementation(projects.coreNavigation)
    implementation(projects.coreNetworking)
    implementation(projects.coreLocalStorage)

    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))

    implementation(libs.bundles.koin)
    implementation(libs.bundles.androidSupport)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.kotlin)

    implementation(libs.lottie)
    testImplementation(libs.bundles.test)
    
    // Karte SDK
    implementation("io.karte.android:core:2.+")
    implementation("io.karte.android:notification:2.+")

    // Kover - Combined report
    rootProject.subprojects.forEach { kover(it) }
}