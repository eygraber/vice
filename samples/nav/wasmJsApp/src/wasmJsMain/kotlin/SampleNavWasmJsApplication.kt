import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.eygraber.vice.samples.nav.shared.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() = CanvasBasedWindow("Todo") {
  App()
}
