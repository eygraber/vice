package com.eygraber.vice.sources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi

@ExperimentalTestApi
class ViceStateRestorationTester(
  private val composeTest: ComposeUiTest,
  private val onDisposedAction: () -> Unit = {},
) {
  private var registry: RestorationRegistry? = null

  /**
   * This functions is a direct replacement for [ComposeUiTest.setContent] if you are going to use
   * [emulateSaveAndRestore] in the test.
   *
   * @see ComposeUiTest.setContent
   */
  fun setContent(composable: @Composable () -> Unit) {
    composeTest.setContent {
      InjectRestorationRegistry { registry ->
        this.registry = registry
        composable()
      }
    }
  }

  /**
   * Emulates a save and restore cycle of the current composition. First all state that is
   * remembered with [rememberSaveable][androidx.compose.runtime.saveable.rememberSaveable] is
   * stored, then the current composition is disposed, and finally the composition is composed
   * again. This allows you to test how your component behaves when state restoration is
   * happening. Note that state stored via [remember] will be lost.
   */
  fun emulateSaveAndRestore() {
    val registry = checkNotNull(registry) { "setContent should be called first!" }
    composeTest.runOnIdle {
      registry.saveStateAndDisposeChildren()
      onDisposedAction()
    }
    composeTest.runOnIdle { registry.emitChildrenWithRestoredState() }
    composeTest.runOnIdle {
      // we just wait for the children to be emitted
    }
  }

  @Composable
  private fun InjectRestorationRegistry(content: @Composable (RestorationRegistry) -> Unit) {
    val restorationRegistry = remember {
      RestorationRegistry(
        SaveableStateRegistry(
          restoredValues = null,
          canBeSaved = { true },
        ),
      )
    }

    CompositionLocalProvider(LocalSaveableStateRegistry provides restorationRegistry) {
      if(restorationRegistry.shouldEmitChildren) {
        content(restorationRegistry)
      }
    }
  }

  private class RestorationRegistry(private val original: SaveableStateRegistry) : SaveableStateRegistry {
    var shouldEmitChildren by mutableStateOf(true)
      private set

    private var currentRegistry: SaveableStateRegistry = original
    private var savedMap: Map<String, List<Any?>> = emptyMap()

    fun saveStateAndDisposeChildren() {
      savedMap = currentRegistry.performSave()
      shouldEmitChildren = false
    }

    fun emitChildrenWithRestoredState() {
      currentRegistry =
        SaveableStateRegistry(
          restoredValues = savedMap,
          canBeSaved = { original.canBeSaved(it) },
        )
      shouldEmitChildren = true
    }

    override fun consumeRestored(key: String) = currentRegistry.consumeRestored(key)

    override fun registerProvider(key: String, valueProvider: () -> Any?) =
      currentRegistry.registerProvider(key, valueProvider)

    override fun canBeSaved(value: Any) = currentRegistry.canBeSaved(value)

    override fun performSave() = currentRegistry.performSave()
  }
}
