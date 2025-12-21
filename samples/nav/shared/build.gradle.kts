import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-kmp-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt2")
  alias(libs.plugins.kotlinxSerialization)
}

group = "samples-nav-shared"

kotlin {
  defaultKmpTargets(
    project = project,
    androidNamespace = "com.eygraber.vice.samples.nav.shared",
  )

  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.samples.shared)
        implementation(projects.viceNav)

        implementation(libs.compose.material3)
        api(libs.compose.materialIcons)

        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
      }
    }
  }
}

gradleConventions.kotlin {
  explicitApiMode = ExplicitApiMode.Disabled
}
