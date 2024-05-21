package com.eygraber.vice.samples.nav.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraph
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.eygraber.vice.nav.viceComposable
import com.eygraber.vice.nav.viceDialog
import com.eygraber.vice.samples.nav.shared.about.AboutUsDestination
import com.eygraber.vice.samples.nav.shared.details.DetailsDestination
import com.eygraber.vice.samples.nav.shared.home.HomeDestination
import com.eygraber.vice.samples.nav.shared.settings.SettingsDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch
import kotlin.jvm.JvmInline

expect fun createHistory(): NeoHistory

@JvmInline
value class Delta(val value: Int)

interface NeoHistory {
  sealed interface Change {
    data object Empty : Change
    data class Navigate(val routes: List<Any>) : Change
    data class Pop(val count: Int) : Change
  }

  var isEnabled: Boolean

  fun init()

  fun push(index: Int)
  fun move(delta: Delta)

  fun update(display: String, state: Any)

  suspend fun awaitChange(): Change
}

val history = createHistory()

@Composable
fun App() {
  val navController = rememberNavController()

  LaunchedEffect(Unit) {
    history.init()

    launch {
      val backStackChanges = navController.currentBackStack.map { stackWithGraphs ->
        stackWithGraphs.filterNot { it.destination is NavGraph }
      }.map { stack ->
        stack.map { it.id }
      }

      backStackChanges.withPrevious().collectLatest { (previousBackstack, currentBackstack) ->
        val lastEqualIndex = findLastEqualIndex(previousBackstack, currentBackstack)
        println("prev=${previousBackstack.size} new=${currentBackstack.size}, lastEqualIndex=$lastEqualIndex")
        if(history.isEnabled) {
          val newItems = currentBackstack.size - (lastEqualIndex + 1)
          val isPushRequired = previousBackstack.isNotEmpty() && newItems > 0

          val delta = (lastEqualIndex + 1) - previousBackstack.size
          if(delta < 0) {
            history.move(Delta(delta))

            // need to await the history event because
            // History doesn't seem to like a move followed immediately by a push
            // we need to consume the event anyways even if we're not about to push
            history.awaitChange()
          }

          if(isPushRequired) {
            repeat(newItems) {
              history.push(lastEqualIndex + 1 + it)
            }
          }
        }
        else {
          println("History wasn't enabled")
        }

        history.isEnabled = true

        println("Awaiting history change")
        when(val change = history.awaitChange()) {
          NeoHistory.Change.Empty -> {
            println("Empty")
          }

          is NeoHistory.Change.Navigate -> {
            history.isEnabled = false
            change.routes.forEach { route ->
              // FIXME: Any <-> String
              navController.navigate(route as String)
            }.also {
              println("Navigate(${change.routes.size})")
            }
          }

          is NeoHistory.Change.Pop -> {
            history.isEnabled = false

            repeat(change.count) {
              navController.popBackStack()
            }.also {
              println("Pop(${change.count})")
            }
          }
        }
      }
    }
  }

  NavHost(
    navController = navController,
    startDestination = "/todo",
  ) {
    viceComposable(
      route = "/todo",
    ) {
      history.update("/todo", it)

      HomeDestination(
        onNavigateToCreateItem = { navController.navigate("/todo/create") },
        onNavigateToUpdateItem = { id -> navController.navigate("/todo/update/$id") },
        onNavigateToSettings = { navController.navigate("/settings") },
      )
    }

    viceDialog(
      route = "/todo/create",
    ) {
      history.update("/todo/create", op)

      DetailsDestination(
        op = Routes.Details.Create,
        onNavigateBack = { navController.popBackStack() },
      )
    }

    viceDialog(
      route = "/todo/update/{id}",
      arguments = listOf(
        navArgument("id") { type = NavType.StringType },
      ),
    ) { op ->
      history.update("/todo/update", op)

      DetailsDestination(
        op = Routes.Details.Update(
          id = requireNotNull(op.arguments?.getString("id")),
        ),
        onNavigateBack = { navController.popBackStack() },
      )
    }

    navigation(
      route = "/settings-home",
      startDestination = "/settings",
    ) {
      viceComposable(
        route = "/settings",
      ) {
        history.update("/settings", it)

        SettingsDestination(
          onNavigateBack = { navController.popBackStack() },
          onNavigateToAboutUs = {
            navController.navigate("/settings/about-us") {
              popUpTo("/settings") {
                inclusive = true
                saveState = true
              }
            }
          }
        )
      }

      viceComposable(
        route = "/settings/about-us",
      ) {
        history.update("/settings/about-us", it)

        AboutUsDestination(
          onNavigateBack = { navController.popBackStack() },
        )
      }
    }
  }
}

private fun Flow<List<String>>.withPrevious() = runningFold(
  emptyList<String>() to emptyList<String>()
) { (_, previous), new ->
  previous to new
}

private fun findLastEqualIndex(left: List<String>, right: List<String>): Int {
  var i = 0
  while(i < left.size && i < right.size) {
    if(left[i] != right[i]) {
      return i - 1
    }

    i++
  }

  return i - 1
}
