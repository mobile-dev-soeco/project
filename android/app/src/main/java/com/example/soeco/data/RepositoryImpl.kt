package com.example.soeco.data

import io.realm.mongodb.AppException
import io.realm.mongodb.User

class RepositoryImpl(
    private val realmDataSource: RealmDataSource
): Repository {

    override fun login(
        email: String,
        password: String,
        loginSuccess: (User) -> Unit,
        loginError: (AppException?) -> Unit
    ) {
        realmDataSource.login(email, password, loginSuccess, loginError)
    }

    override fun logout(logoutSuccess: () -> Unit, logoutError: (Throwable?) -> Unit) {
        realmDataSource.logOut(logoutSuccess, logoutError)
    }

    override fun getUserRole(): String {
        return realmDataSource.getUserRole()
    }

    override fun restoreLoggedInUser() = realmDataSource.restoreLoggedInUser()
}