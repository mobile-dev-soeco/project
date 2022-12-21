package com.example.soeco.data

import android.content.Context
import android.util.Log
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.AppException
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

class RealmDataSource(context: Context) {

    private val realmApp: App
    private val APP_ID = "soecoapp-ciaaa"

    private lateinit var realm: Realm
    private lateinit var currentRealmUser: io.realm.mongodb.User

    init {
        Log.v("Realm Data", "Created a new instance of Realm data source")
        Realm.init(context)
        realmApp = App(AppConfiguration.Builder(APP_ID).build())
    }

    fun login(
        email: String,
        password: String,
        loginSuccess: (User) -> Unit,
        loginError: (AppException?) -> Unit
    ) {
        val userCredentials = Credentials.emailPassword(email, password)
        realmApp.loginAsync(userCredentials) {
            if (it.error != null){
                loginError.invoke(it.error)
            } else {
                val user = it.get()
                instantiateRealm(user)
                loginSuccess.invoke(user)
            }
        }
    }

    fun logOut(
        logoutSuccess: () -> Unit,
        logoutError: (Throwable?) -> Unit
    ) {
        realmApp.currentUser()?.logOutAsync {
            if (it.error != null) {
                logoutError.invoke(it.error.exception)
            } else {
                logoutSuccess.invoke()
            }
        }
    }

    fun getUserRole(): String {
        return currentRealmUser.customData["role"].toString()
    }

    private fun instantiateRealm(user: User) {
        currentRealmUser = user

        val userData = user.customData
        val userRole = userData["role"]

        // Can pass in a partition to the Builder method if needed.
        val configuration = SyncConfiguration
            .Builder(user)
            .build()

        realm = Realm.getInstance(configuration)
    }

    fun restoreLoggedInUser(): io.realm.mongodb.User? {
        return realmApp.currentUser()?.also {
            instantiateRealm(it)
        }
    }

}