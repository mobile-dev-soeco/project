package com.example.soeco.data.Models.DB_Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId


open class Product_Report_DB : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    @Required
    var product_id: String =""
    var orderNumber: String = ""

    var product_count : Int = 0
    var product_name : String = ""

    var date: String =""
    var time: String = ""



    constructor(
        product_id:String  = "",
        orderNumber: String ="",

        product_count: Int =0,
        product_name: String ="",

        date : String = "",
        time: String = "",

        ) {
        this.product_id = product_id
        this.orderNumber = orderNumber

        this.product_count = product_count
        this.product_name = product_name

        this.date = date
        this.time = time

    }
    constructor() {} // RealmObject subclasses must provide an empty constructor
}