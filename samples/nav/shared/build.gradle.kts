import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
  alias(libs.plugins.kotlinxSerialization)
}

group = "samples-nav-shared"

android {
  namespace = "com.eygraber.vice.samples.nav.shared"
}

kotlin {
  defaultKmpTargets(
    project = project,
  )

  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.samples.shared)
        implementation(projects.viceNav)

        implementation(compose.material3)
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
