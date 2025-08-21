import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
  id("com.eygraber.conventions-publish-maven-central")
}

android {
  namespace = "com.eygraber.vice.nav3"
}

kotlin {
  kmpTargets(
    KmpTarget.Android,
    project = project,
    ignoreDefaultTargets = true,
  )

  sourceSets {
    androidMain.dependencies {
      api(libs.compose.nav3.runtime)
      api(libs.compose.nav3.ui)
    }

    commonMain.dependencies {
      api(projects.viceCore)

      implementation(compose.animation)
      implementation(compose.runtime)

      implementation(libs.kotlinx.coroutines.core)
    }

    commonTest.dependencies {
      implementation(kotlin("test-common"))
      implementation(kotlin("test-annotations-common"))
    }
  }

  @OptIn(ExperimentalAbiValidation::class)
  abiValidation.enabled = true
}
