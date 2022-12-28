package com.example.soeco.data.Models.API_Models

class Product(var id :Int, var count:Int) {
    override fun toString(): String{
        return "$count $id"
    }
}