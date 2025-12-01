package com.melodym3.service

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton service to manage UI-level messages (like Snackbars).
 * ViewModels or Repositories can inject this service and call showMessage 
 * to send a non-intrusive notification to the main UI thread.
 */
@Singleton
class SnackbarManager @Inject constructor() {

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    /**
     * Sends a message string to the UI layer to be displayed in a Snackbar.
     */
    suspend fun showMessage(message: String) {
        _messages.emit(message)
    }
}
