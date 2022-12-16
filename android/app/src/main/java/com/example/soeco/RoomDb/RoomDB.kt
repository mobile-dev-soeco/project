package com.example.soeco.RoomDb

import android.content.Context
import androidx.room.*
import com.example.soeco.Models.API_Models.Product
import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.Models.DB_Models.Product_DB

// entities = tables

@Database(entities = [Material_DB::class, Order_DB::class, Product_DB::class ],
    version = 3, exportSchema = false)
@TypeConverters(Converters::class)
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

class Converters {

    @TypeConverter
    fun fromString(stringListString: String?): ArrayList<String> {
        val result = ArrayList<String>()
        val stringSplit = stringListString?.split(",")
        if (stringSplit != null) {
            for (string in stringSplit)
                result.add(string)
        }
        return result
    }

    @TypeConverter
    fun toString(stringList: ArrayList<String>?): String? {
        return stringList?.joinToString(separator = ",")
    }
    @TypeConverter
    fun fromStringProduct(stringListString: String): ArrayList<Product> {
        val result = ArrayList<Product>()
        val stringSplit = stringListString.split(",")
        for (string in stringSplit){
            val split= string.split(" ")
            result.add(Product(split[0], split[1].toInt()))
        }
        return result
    }

    @TypeConverter
    fun toStringProduct(productList: ArrayList<Product>): String {
        return productList.joinToString(separator = ",")
    }

}

