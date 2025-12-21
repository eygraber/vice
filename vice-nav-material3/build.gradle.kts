import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-kmp-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt2")
  id("com.eygraber.conventions-publish-maven-central")
}

kotlin {
  defaultKmpTargets(
    project = project,
    androidNamespace = "com.eygraber.vice.nav.material3",
  )

  sourceSets {
    commonMain.dependencies {
      api(projects.viceNav)

      implementation(libs.compose.material3)
      api(libs.compose.navigation)
      api(libs.compose.navigationMaterial3)
      implementation(libs.compose.runtime)
    }
  }

  @OptIn(ExperimentalAbiValidation::class)
  abiValidation.enabled = true
}
