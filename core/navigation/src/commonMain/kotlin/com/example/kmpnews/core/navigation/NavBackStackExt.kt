package com.example.kmpnews.core.navigation

import androidx.navigation3.runtime.NavKey

fun MutableList<NavKey>.execute(command: NavigationCommand) {
    when (command) {
        is NavigationCommand.NavigateTo -> navigateTo(
            destination = command.to,
            clearBackStack = command.clearBackStack,
            addToBackStack = command.addToBackStack,
        )

        NavigationCommand.NavigateUp -> navigateUp()

        is NavigationCommand.PopBackStackTo -> popBackStackTo(
            destination = command.to,
            inclusive = command.inclusive,
        )

        is NavigationCommand.Destination -> add(command)
    }
}

private fun MutableList<NavKey>.navigateTo(
    destination: NavigationCommand.Destination,
    clearBackStack: Boolean,
    addToBackStack: Boolean,
) {
    if (clearBackStack) {
        clear()
    } else if (!addToBackStack && isNotEmpty()) {
        removeLastOrNull()
    }
    add(destination)
}

private fun MutableList<NavKey>.navigateUp() {
    if (size > 1) {
        removeLastOrNull()
    }
}

private fun MutableList<NavKey>.popBackStackTo(
    destination: NavigationCommand.Destination,
    inclusive: Boolean,
) {
    val index = indexOfLast { it == destination }
    if (index < 0) return
    val lastIndexToKeep = if (inclusive) index else index + 1
    while (size > lastIndexToKeep && size > 1) {
        removeAt(lastIndex)
    }
}
