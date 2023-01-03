package com.example.soeco.data.Models.DB_Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId



open class Deviation_Report_DB : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    @Required
    var date: String = ""
    var time: String = ""

    var product_id: String? =""
    var problem: String =""
    var solution: String =""
    var cost: String = ""

    var orderNumber: String = ""


    constructor(
        date:String  = "",
        time: String = "",
        product_id: String? ="",
        problem: String = "",
        solution : String = "",
        cost: String ="",
        orderNumber: String = ""

    ) {
        this.date = date
        this.time = time
        this.product_id = product_id
        this.problem = problem
        this.solution = solution
        this.cost = cost
        this.orderNumber = orderNumber

    }
    constructor() {} // RealmObject subclasses must provide an empty constructor

    override fun toString(): String {
        return "$date $time $product_id $problem $solution $cost $orderNumber"
    }


}