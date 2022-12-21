package com.example.soeco.data

import io.realm.mongodb.AppException
import io.realm.mongodb.User

interface Repository {

    fun login(
        email: String,
        password: String,
        loginSuccess: (User) -> Unit,
        loginError: (AppException?) -> Unit
    )

    fun logout(logoutSuccess: () -> Unit, logoutError: (Throwable?) -> Unit)

    fun getUserRole(): String

    fun restoreLoggedInUser(): User?

}