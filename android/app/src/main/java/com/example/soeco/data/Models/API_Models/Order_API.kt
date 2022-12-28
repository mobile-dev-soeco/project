package com.example.soeco.data.Models.API_Models

class Order_API (var OrderNumber: String,
                 var Products:ArrayList<Product>,
                 var expectHours:Int,
                 var address:String,
                 var contact :ArrayList<String>)
{
    override fun toString (): String {
        return OrderNumber
    }
}



