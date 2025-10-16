import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.eygraber.vice.samples.nav.shared.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() = ComposeViewport {
  App()
}
