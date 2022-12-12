package com.example.soeco

import android.app.Application
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration

lateinit var realmAppServices: App

inline fun <reified T> T.TAG(): String = T::class.java.simpleName

class RealmApplication: Application() {

    val appId: String = "soecoapp-ciaaa"

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        realmAppServices = App(AppConfiguration.Builder(appId).build())
    }
}