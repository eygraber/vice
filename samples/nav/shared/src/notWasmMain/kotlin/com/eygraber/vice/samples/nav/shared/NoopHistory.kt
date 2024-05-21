package com.eygraber.vice.samples.nav.shared

actual fun createHistory(): NeoHistory = NoopNeoHistory

object NoopNeoHistory : NeoHistory {
  private val emptyChange = NeoHistory.Change.Empty

  override var isEnabled: Boolean = false

  override fun init() {}

  override fun push(index: Int) {}

  override fun move(delta: Delta) {}

  override fun update(display: String, state: Any) {}

  override suspend fun awaitChange(): NeoHistory.Change = emptyChange
}
