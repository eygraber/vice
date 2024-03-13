import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
  id("com.eygraber.conventions-publish-maven-central")
}

android {
  namespace = "com.eygraber.vice"
}

kotlin {
  defaultKmpTargets(
    project = project,
  )

  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  applyDefaultHierarchyTemplate {
    common {
      group("notAndroid") {
        withIos()
        withJs()
        withJvm()
        withWasm()
      }
    }
  }

  sourceSets {
    androidMain {
      dependencies {
        implementation(libs.compose.lifecycle)
      }
    }

    commonMain {
      dependencies {
        implementation(compose.runtime)

        implementation(libs.kotlinx.coroutines.core)
      }
    }

    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
  }
}
