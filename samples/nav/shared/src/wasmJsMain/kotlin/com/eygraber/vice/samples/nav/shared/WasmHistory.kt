package com.eygraber.vice.samples.nav.shared

import androidx.collection.SparseArrayCompat
import androidx.collection.keyIterator
import androidx.collection.set
import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import org.w3c.dom.PopStateEvent
import org.w3c.dom.Window
import org.w3c.dom.events.Event
import kotlin.coroutines.resume

actual fun createHistory(): NeoHistory = WasmNeoHistory()

external interface HistoryState : JsAny {
  val index: Int
}

@Suppress("UNUSED_PARAMETER")
fun historyState(index: Int): JsAny = js("({ index: index })")

class WasmNeoHistory : NeoHistory {
  private val Window.historyStateIndex get() = history.state?.unsafeCast<HistoryState>()?.index ?: 0

  private val states = SparseArrayCompat<Any>()

  override var isEnabled: Boolean = true

  override fun init() {
    window.history.replaceState(historyState(0), "", "")
  }

  override fun push(index: Int) {
    println("push($index)")
    window.history.pushState(historyState(index), "", "")
    states.keyIterator().asSequence().filter { it > index }.forEach { states.remove(it) }
  }

  override fun move(delta: Delta) {
    println("move(${delta.value})")
    window.history.go(delta.value)
  }

  override fun update(display: String, state: Any) {
    println("update(${window.historyStateIndex}, $display, $state)")
    window.history.replaceState(window.history.state, "", display)
    states[window.historyStateIndex] = state
  }

  override suspend fun awaitChange(): NeoHistory.Change =
    suspendCancellableCoroutine { cont ->
      val currentIndex = window.historyStateIndex

      var listener: ((Event) -> Unit)? = null
      listener = { event ->
        if(listener != null) {
          window.removeEventListener("popstate", listener)
          listener = null
          val state = (event as PopStateEvent).state?.unsafeCast<HistoryState>()
          if(state != null) {
            val delta = state.index - currentIndex
            val change = when {
              delta < 0 -> NeoHistory.Change.Pop(currentIndex - state.index)

              delta > 0 -> NeoHistory.Change.Navigate(
                ((currentIndex + 1)..state.index).mapNotNull { index ->
                  states[index]
                }
              )

              else -> NeoHistory.Change.Empty
            }

            cont.resume(change)
          }
        }
      }

      window.addEventListener("popstate", listener)

      cont.invokeOnCancellation {
        window.removeEventListener("popstate", listener)
        listener = null
      }
    }
}
