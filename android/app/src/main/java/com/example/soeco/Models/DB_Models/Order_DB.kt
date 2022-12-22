package com.example.soeco.Models.DB_Models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Order_DB(
    @PrimaryKey
    var orderNumber: String ="",
    var Products: RealmList<String>? =null,
    var expectHours: Int? = 0,
    var address: String? = "",
    var contact: RealmList<String>? = null
): RealmObject()











