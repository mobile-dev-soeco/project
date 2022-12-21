package com.example.soeco.Models.DB_Models

import com.example.soeco.Models.API_Models.Product
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId
import java.lang.reflect.Type


open class Order_DB
    (
    @PrimaryKey
    var OrderNumber: String ="",
    var Products: RealmList<String>? =null,
    var expectHours: Int? = 0,
    var address: String? = "",
    var contact: RealmList<String>? = null,
): RealmObject()














