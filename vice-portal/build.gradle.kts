import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
  id("com.eygraber.conventions-publish-maven-central")
}

android {
  namespace = "com.eygraber.vice.portal"
}

kotlin {
  defaultKmpTargets(
    project = project,
  )

  sourceSets {
    commonMain {
      dependencies {
        api(projects.viceCore)

        implementation(compose.runtime)

        implementation(libs.kotlinx.coroutines.core)

        api(libs.portalCompose)
      }
    }

    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
  }

  @OptIn(ExperimentalAbiValidation::class)
  abiValidation.enabled = true
}
