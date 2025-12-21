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
    androidNamespace = "com.eygraber.vice.sources",
  )

  sourceSets {
    commonMain {
      dependencies {
        api(projects.viceCore)

        api(libs.compose.runtime)
        api(libs.compose.runtimeSaveable)

        implementation(libs.lifecycle.compose)

        api(libs.kotlinx.coroutines.core)
      }
    }

    commonTest {
      dependencies {
        implementation(kotlin("test"))
      }
    }

    jvmTest {
      dependencies {
        implementation(compose.desktop.currentOs)

        implementation(libs.compose.foundation)
        implementation(libs.compose.uiTest)

        implementation(libs.test.kotlinx.coroutines)
        implementation(libs.test.turbine)
      }
    }
  }

  @OptIn(ExperimentalAbiValidation::class)
  abiValidation.enabled = true
}
