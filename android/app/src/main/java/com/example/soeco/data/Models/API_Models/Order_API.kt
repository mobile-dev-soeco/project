package com.example.soeco.data.Models.API_Models

class Order_API (
                 var idoo: Int,
                 var oo_nr: String,
                 var costumers_name :String,
                 var address:String,
                 var zip:String,
                 var city:String,
                 var contact :String,
                 var delivery_planning_employees:String,
                 var delivery_planning_turn:String,




                 )
{
    override fun toString (): String {
        return oo_nr
    }
}



