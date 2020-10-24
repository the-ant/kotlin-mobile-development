plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}
group = "com.example.demo"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}
dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    implementation("io.ktor:ktor-client-cio:1.4.0")
    implementation("io.ktor:ktor-client-okhttp:1.4.0")
    implementation("io.ktor:ktor-client-gson:1.4.0")
}
android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.example.demo.androidApp"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}