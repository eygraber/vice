import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
  alias(libs.plugins.kotlinxSerialization)
}

group = "samples-nav3-shared"

android {
  namespace = "com.eygraber.vice.samples.nav3.shared"
}

kotlin {
  kmpTargets(
    KmpTarget.Android,
    project = project,
    ignoreDefaultTargets = true,
  )

  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.samples.shared)
        implementation(projects.viceNav3)

        implementation(libs.compose.material3)
        api(libs.compose.materialIcons)
        api(libs.compose.nav3.ui)

        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
      }
    }
  }
}

gradleConventions.kotlin {
  explicitApiMode = ExplicitApiMode.Disabled
}
