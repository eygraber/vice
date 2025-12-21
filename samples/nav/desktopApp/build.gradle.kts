import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
  kotlin("jvm")
  id("com.eygraber.conventions-kotlin-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt2")
}

group = "samples-nav-desktop"

dependencies {
  implementation(projects.samples.nav.shared)
  implementation(compose.desktop.currentOs)
}

compose.desktop {
  application {
    mainClass = "com.eygraber.vice.samples.nav.desktop.SampleNavDesktopAppKt"
  }
}

gradleConventions.kotlin {
  explicitApiMode = ExplicitApiMode.Disabled
}
