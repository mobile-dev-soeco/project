package com.example.soeco.data.Models.API_Models

class Order_API (var oo_nr: String,
                 var idoo: Int,
                 var address:String,
                 var costumers_name :String,



                 )
{
    override fun toString (): String {
        return oo_nr
    }
}



