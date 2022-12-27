package com.example.soeco.data

import android.content.Context

object ServiceLocator {
    @Volatile
    private var repository: Repository? = null

    fun provideRepository(context: Context): Repository {
        synchronized(this) {
            return repository
                ?: createRepository(context.applicationContext)
        }
    }

    private fun createRepository(context: Context): Repository {
        repository = RepositoryImpl(
            RealmDataSource(context)
        )
        return repository as Repository
    }
}