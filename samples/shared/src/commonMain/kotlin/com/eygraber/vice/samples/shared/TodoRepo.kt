package com.eygraber.vice.samples.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

object TodoRepo {
  private val mutableItems = MutableStateFlow<List<TodoItem>>(emptyList())

  val items: StateFlow<List<TodoItem>> = mutableItems

  fun findItem(
    id: String,
  ): TodoItem? = items.value.find { it.id == id }

  fun addItem(
    title: String,
    description: String,
  ) {
    mutableItems.value += TodoItem(
      id = Random.nextInt().toString(),
      isCompleted = false,
      title = title,
      description = description,
    )
  }

  fun updateItem(
    newItem: TodoItem,
  ) {
    mutableItems.value = buildList {
      for(existingItem in mutableItems.value) {
        if(existingItem.id == newItem.id) {
          add(newItem)
        }
        else {
          add(existingItem)
        }
      }
    }
  }

  fun removeItem(
    item: TodoItem,
  ) {
    mutableItems.value = mutableItems.value.filterNot { it.id == item.id }
  }
}
