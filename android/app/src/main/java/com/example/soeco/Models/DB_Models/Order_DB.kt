package com.example.soeco.Models.DB_Models

import androidx.room.*
import com.example.soeco.Models.API_Models.Product
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Entity(tableName = "orderTable")
class Order_DB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "OrderNumber") var orderNumber: String,

    @ColumnInfo(name = "Products") var products: ArrayList<Product>,

    @ColumnInfo(name = "ExpectHours") var expectHours: Int? = null,

    @ColumnInfo(name = "Address") var address: String? = null,

    @ColumnInfo(name = "Contact") var contact: ArrayList<String>? = null)










