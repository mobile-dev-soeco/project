package com.example.soeco.data.Models.DB_Models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Order_DB
    (
    @PrimaryKey
    var _id: String ="",
    var expectHours: Int? = 0,
    var address: String? = "",
    var contact: RealmList<String>? = null,
): RealmObject()














