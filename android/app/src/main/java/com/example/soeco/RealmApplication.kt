package com.example.soeco

import android.app.Application
import com.example.soeco.data.Repository
import com.example.soeco.data.ServiceLocator

inline fun <reified T> T.TAG(): String = T::class.java.simpleName

class RealmApplication: Application(){

    val repository: Repository
        get() = ServiceLocator.provideRepository(this)


}