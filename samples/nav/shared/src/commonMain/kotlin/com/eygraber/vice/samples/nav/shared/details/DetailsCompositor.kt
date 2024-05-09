package com.eygraber.vice.samples.nav.shared.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.samples.nav.shared.Routes
import com.eygraber.vice.samples.nav.shared.TodoRepo

class DetailsCompositor(
  op: Routes.Details,
  private val onNavigateBack: () -> Unit,
) : ViceCompositor<DetailsIntent, DetailsViewState>() {
  private val item = when(op) {
    Routes.Details.Create -> null
    is Routes.Details.Update -> TodoRepo.findItem(op.id)
  }

  private var title by mutableStateOf(item?.title.orEmpty())
  private var titleError: String? by mutableStateOf(null)
  private var description by mutableStateOf(item?.description.orEmpty())
  private var descriptionError: String? by mutableStateOf(null)
  private val isDoneEnabled by derivedStateOf {
    title.isNotEmpty() && description.isNotEmpty()
  }

  @Composable
  override fun composite() = DetailsViewState(
    title = title,
    titleError = titleError,
    description = description,
    descriptionError = descriptionError,
    isDoneEnabled = isDoneEnabled,
  )

  override suspend fun onIntent(intent: DetailsIntent) {
    when(intent) {
      is DetailsIntent.NavigateBack -> onNavigateBack()

      is DetailsIntent.TitleUpdated -> {
        title = intent.title
        titleError = "Cannot be empty".takeIf { title.isBlank() }
      }

      is DetailsIntent.DescriptionUpdated -> {
        description = intent.description
        descriptionError = "Cannot be empty".takeIf { description.isBlank() }
      }

      is DetailsIntent.Done -> when(item) {
        null -> TodoRepo.addItem(
          title = title.trim(),
          description = description.trim(),
        )

        else -> TodoRepo.updateItem(
          item.copy(
            title = title.trim(),
            description = description.trim(),
          ),
        )
      }.also {
        onNavigateBack()
      }
    }
  }
}
