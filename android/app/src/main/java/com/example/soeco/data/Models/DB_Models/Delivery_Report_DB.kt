package com.example.soeco.data.Models.DB_Models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class Delivery_Report_DB : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    @Required
    var time: String = ""

    var selected: RealmList<Product_DB> = RealmList()
    var notselected: RealmList<Product_DB> = RealmList()

    var orderExpectedTime : String= ""
    var orderNumber: String = ""


    constructor(
        time:String  = "",
        selected: RealmList<Product_DB> = RealmList(),
        notselected: RealmList<Product_DB> = RealmList(),
        orderExpectedTime: String = "",
        orderNumber: String = "",


    ) {
        this.time = time
        this.selected = selected
        this.notselected = notselected
        this.orderExpectedTime=orderExpectedTime
        this.orderNumber = orderNumber

    }
    constructor() {} // RealmObject subclasses must provide an empty constructor




}