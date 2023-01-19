package com.example.soeco.utils

sealed class ActionResult {
    object Success: ActionResult()
    object Error: ActionResult()
    object Handled: ActionResult()
}