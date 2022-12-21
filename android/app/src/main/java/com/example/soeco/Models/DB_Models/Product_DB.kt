package com.example.soeco.Models.DB_Models


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId



open class Product_DB(
    @PrimaryKey
    var id: Int = 0,
    @Required
    var name: String ="",
    var expectHours: Int=0,
): RealmObject()



