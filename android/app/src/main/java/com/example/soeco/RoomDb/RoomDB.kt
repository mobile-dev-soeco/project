package com.example.soeco.RoomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.Models.DB_Models.Product_DB
// entities = tables

@Database(entities = [Material_DB::class, Order_DB::class, Product_DB::class ],
    version = 1, exportSchema = false)

abstract class RoomDB : RoomDatabase() {
    // dao = class with db queries
    abstract fun dao(): DAO

    // singleton bs
    companion object {
        private var INSTANCE: RoomDB? = null
        internal fun getDatabase(context: Context): RoomDB? {
            if (INSTANCE == null) { synchronized(RoomDB::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext, RoomDB::class.java, "RoomDB").build()
                    }
                }
            }
            return INSTANCE
        }
    }
}

