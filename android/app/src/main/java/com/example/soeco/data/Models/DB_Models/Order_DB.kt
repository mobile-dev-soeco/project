package com.example.soeco.data.Models.DB_Models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required


open class Order_DB: RealmObject{
    @PrimaryKey
    var _id: String =""
    @Required
    var idoo :Int? = 0
    var expectHours: Int? = 0
    var address:String?=""
    var zip:String?=""
    var city:String?=""
    var phone :String?=""
    var name :String?=""


    constructor(
        orderNumber: String = "",
        idoo: Int = 0,
        expectHours: Int =0,
        address: String = "",
        zip: String = "",
        city:String = "",
        phone : String = "",
        name: String = ""
    ){
        this._id = orderNumber
        this.idoo = idoo
        this.expectHours = expectHours
        this.address = address
        this.zip = zip
        this.city = city
        this.phone = phone
        this.name = name

    }
    constructor() {}


}
















