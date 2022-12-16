package com.example.soeco.Models.DB_Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.soeco.Models.API_Models.Product


@Entity(tableName = "productTable")

class Product_DB (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "expectHours") var expectHours: Int)