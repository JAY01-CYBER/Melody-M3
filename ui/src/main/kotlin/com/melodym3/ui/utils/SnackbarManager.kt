package com.melodym3.ui.utils

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SnackbarManager {
    private val snackbarHostStates = mutableListOf<SnackbarHostState>()
    
    fun addSnackbarHostState(hostState: SnackbarHostState) {
        snackbarHostStates.add(hostState)
    }
    
    fun removeSnackbarHostState(hostState: SnackbarHostState) {
        snackbarHostStates.remove(hostState)
    }
    
    fun showMessage(message: String, scope: CoroutineScope? = null) {
        val hostState = snackbarHostStates.lastOrNull()
        if (hostState != null) {
            if (scope != null) {
                scope.launch {
                    hostState.showSnackbar(message)
                }
            } else {
                println("Snackbar: $message")
            }
        } else {
            println("No SnackbarHostState available: $message")
        }
    }
    
    fun showError(message: String, scope: CoroutineScope? = null) {
        showMessage("Error: $message", scope)
    }
}
