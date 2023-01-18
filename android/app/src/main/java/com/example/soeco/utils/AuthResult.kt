package com.example.soeco.utils

sealed class AuthResult {
    object Success: AuthResult()
    object Error: AuthResult()
    object Handled: AuthResult()
}