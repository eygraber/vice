import org.jetbrains.compose.ExperimentalComposeLibrary

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

  sourceSets {
    androidMain {
      dependencies {
        implementation(libs.kotlinx.coroutines.android)
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
        implementation(kotlin("test"))
      }
    }

    jvmTest {
      dependencies {
        implementation(compose.desktop.currentOs)

        implementation(compose.foundation)

        @OptIn(ExperimentalComposeLibrary::class)
        implementation(compose.uiTest)
      }
    }

    jvmMain {
      dependencies {
        implementation(libs.kotlinx.coroutines.swing)
      }
    }
  }
}
