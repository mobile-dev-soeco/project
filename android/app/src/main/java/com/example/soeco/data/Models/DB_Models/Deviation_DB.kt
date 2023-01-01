package com.example.soeco.data.Models.DB_Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId



open class Deviation_DB : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    @Required
    var date: String = ""
    var product_id: String =""
    var problem: String =""
    var solution: String =""
    var cost: Int = 0

    var orderNumber: String = ""


    constructor(
        date:String  = "",
        product_id: String ="",
        problem: String = "",
        solution : String = "",
        cost: Int = 0,
        orderNumber: String = ""

    ) {
        this.date = date
        this.product_id = product_id
        this.problem = problem
        this.solution = solution
        this.cost = cost
        this.orderNumber = orderNumber

    }
    constructor() {} // RealmObject subclasses must provide an empty constructor
}