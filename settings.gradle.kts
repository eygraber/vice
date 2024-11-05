import com.eygraber.conventions.Env
import com.eygraber.conventions.repositories.addCommonRepositories

pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("androidx.*")
      }
    }

    mavenCentral()

    maven("https://oss.sonatype.org/content/repositories/snapshots") {
      mavenContent {
        snapshotsOnly()
      }
    }

    maven("https://s01.oss.sonatype.org/content/repositories/snapshots") {
      mavenContent {
        snapshotsOnly()
      }
    }

    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")

    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") {
      content {
        includeGroupByRegex("org\\.jetbrains.*")
      }
    }

    gradlePluginPortal()
  }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  // comment this out for now because it doesn't work with KMP js
  // https://youtrack.jetbrains.com/issue/KT-55620/KJS-Gradle-plugin-doesnt-support-repositoriesMode
  // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

  repositories {
    addCommonRepositories(
      includeMavenCentral = true,
      includeMavenCentralSnapshots = true,
      includeGoogle = true,
      includeJetbrainsComposeDev = true,
    )

    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
  }
}

plugins {
  id("com.eygraber.conventions.settings") version "0.0.79"
  id("com.gradle.develocity") version "3.18.2"
}

rootProject.name = "vice"

include(":samples:nav:androidApp")
include(":samples:nav:desktopApp")
include(":samples:nav:shared")
include(":samples:nav:wasmJsApp")
include(":vice-core")
include(":vice-nav")
include(":vice-nav-material3")
include(":vice-portal")
include(":vice-sources")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

develocity {
  buildScan {
    termsOfUseUrl = "https://gradle.com/terms-of-service"
    publishing.onlyIf { Env.isCI }
    if(Env.isCI) {
      termsOfUseAgree = "yes"
    }
  }
}
