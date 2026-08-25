import com.eygraber.conventions.tasks.deleteRootBuildDirWhenCleaning
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

buildscript {
  dependencies {
    classpath(libs.buildscript.android)
    classpath(libs.buildscript.androidCacheFix)
    classpath(libs.buildscript.compose.compiler)
    classpath(libs.buildscript.compose.jetbrains)
    classpath(libs.buildscript.detekt)
    classpath(libs.buildscript.dokka)
    classpath(libs.buildscript.kotlin)
    classpath(libs.buildscript.publish)
  }
}

plugins {
  base
  alias(libs.plugins.conventions)
}

deleteRootBuildDirWhenCleaning()

gradleConventionsDefaults {
  android {
    sdkVersions(
      compileSdk = libs.versions.android.sdk.compile,
      targetSdk = libs.versions.android.sdk.target,
      minSdk = libs.versions.android.sdk.min,
    )
  }

  kotlin {
    jvmTargetVersion = JvmTarget.JVM_11
    explicitApiMode = ExplicitApiMode.Strict
  }
}

// Compose 1.12.0 registers checkComposeUiTestConfigurationFor{Js,WasmJs} tasks that fail the web
// browser test tasks of any target whose test compilation has Skiko on its classpath without
// binaries.executable() being declared. The modules that trip it (anything depending on Compose UI)
// have no web test sources at all, so their browser test tasks are NO-SOURCE and the check only
// reports false positives. Declaring executables just to satisfy it would add production webpack
// bundles (and the topLevelAwait config that Skiko needs) to every library module.
// If web UI tests are ever added, drop this and add binaries.executable() to those targets instead.
// https://youtrack.jetbrains.com/issue/CMP-4906
subprojects {
  tasks.configureEach {
    if(name.startsWith("checkComposeUiTestConfigurationFor")) {
      enabled = false
    }
  }
}

gradleConventionsKmpDefaults {
  webOptions = webOptions.copy(
    isBrowserEnabled = true,
    isNodeEnabled = false,
  )

  targets(
    KmpTarget.Android,
    KmpTarget.Ios,
    KmpTarget.Js,
    KmpTarget.Jvm,
    KmpTarget.WasmJs,
  )
}
