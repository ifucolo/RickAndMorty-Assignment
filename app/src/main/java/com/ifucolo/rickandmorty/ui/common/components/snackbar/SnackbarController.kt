package com.ifucolo.rickandmorty.ui.common.components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class SnackbarController internal constructor(
    val hostState: SnackbarHostState,
    private val scope: CoroutineScope
) {
    fun show(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onResult: ((SnackbarResult) -> Unit)? = null
    ) {
        scope.launch {
            val result = hostState.showSnackbar(message = message, actionLabel = actionLabel, duration = duration)
            onResult?.invoke(result)
        }
    }
}

/** Remember a controller you can use to trigger snackbars. */
@Composable
fun rememberSnackbarController(): SnackbarController {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    return remember(hostState, scope) { SnackbarController(hostState, scope) }
}

/** Convenience host to plug into Scaffold. */
@Composable
fun AppSnackbarHost(controller: SnackbarController) {
    SnackbarHost(hostState = controller.hostState)
}