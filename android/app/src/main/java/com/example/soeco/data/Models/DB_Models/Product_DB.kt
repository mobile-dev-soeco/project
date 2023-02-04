package com.example.soeco.data.Models.DB_Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class Product_DB : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    @Required
    var product_id: String = ""
    var name: String =""
    var orderNumber: String = ""
    var count: String = ""
    var note : String = ""

    constructor(
        product_id: String = "",
        name: String ="",
        orderNumber: String = "",
        count: String = "",
        note : String = ""

    ) {
        this.product_id = product_id
        this.name = name
        this.orderNumber = orderNumber
        this.count = count
        this.note = note

    }
    constructor() {} // RealmObject subclasses must provide an empty constructor

    override fun toString():String{
        return  product_id
    }
}