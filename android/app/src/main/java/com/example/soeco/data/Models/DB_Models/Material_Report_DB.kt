package com.example.soeco.data.Models.DB_Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId


open class Material_Report_DB : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    @Required
    var orderNumber: String = ""
    var material_ID: String = ""

    var amount: String = ""
    var name: String = ""
    var unit : String = ""

    var date: String =""


    constructor(
        orderNumber: String ="",
        material_ID: String ="",
        amount: String = "",
        name: String = "",
        unit: String ="",
        date : String = "",

        ) {
        this.orderNumber = orderNumber
        this.material_ID = material_ID
        this.amount = amount
        this.name = name
        this.unit = unit
        this.date = date

    }
    constructor() {} // RealmObject subclasses must provide an empty constructor
}