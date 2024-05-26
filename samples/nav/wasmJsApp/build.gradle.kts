import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
}

kotlin {
  kmpTargets(
    KmpTarget.WasmJs,
    project = project,
    binaryType = BinaryType.Executable,
    webOptions = KmpTarget.WebOptions(
      isNodeEnabled = false,
      isBrowserEnabled = true,
      moduleName = "nav-sample-wasm",
    ),
    ignoreDefaultTargets = true,
  )
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser {
      commonWebpackConfig {
        outputFileName = "nav-sample-wasm.js"
        experiments += "topLevelAwait"
      }
    }
  }

  sourceSets {
    wasmJsMain {
      dependencies {
        implementation(projects.samples.nav.shared)

        implementation(compose.material3)
      }
    }
  }
}

gradleConventions.kotlin {
  explicitApiMode = ExplicitApiMode.Disabled
}
