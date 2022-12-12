package com.example.soeco.Models.DB_Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "orderTable")

class Order_DB  (
    @ColumnInfo(name = "title") var title: String,

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

}