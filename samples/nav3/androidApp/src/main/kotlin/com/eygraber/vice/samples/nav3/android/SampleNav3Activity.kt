package com.eygraber.vice.samples.nav3.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.eygraber.vice.samples.nav3.shared.App

class SampleNav3Activity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      App()
    }
  }
}
