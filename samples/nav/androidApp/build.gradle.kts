import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
  id("com.android.application")
  kotlin("android")
  id("com.eygraber.conventions-kotlin-library")
  id("com.eygraber.conventions-compose-jetpack")
  id("com.eygraber.conventions-detekt")
  alias(libs.plugins.kotlinxSerialization)
}

group = "samples-nav-android"

android {
  compileSdk = libs.versions.android.sdk.compile.get().toInt()

  namespace = "com.eygraber.vice.samples.nav.android"

  defaultConfig {
    applicationId = "com.eygraber.vice.samples.nav.android"
    minSdk = libs.versions.android.sdk.min.get().toInt()
    targetSdk = libs.versions.android.sdk.target.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }

  lint {
    checkDependencies = true
    checkReleaseBuilds = false
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }

  composeOptions.kotlinCompilerExtensionVersion = "1.5.14"
}

dependencies {
  // implementation(projects.samples.nav.shared)

  implementation(libs.androidx.activity.compose)
  implementation("androidx.compose.material3:material3:1.3.0-beta01")
  implementation(projects.viceNav)

  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.kotlinx.serialization.json)
}

gradleConventions.kotlin {
  explicitApiMode = ExplicitApiMode.Disabled
}
