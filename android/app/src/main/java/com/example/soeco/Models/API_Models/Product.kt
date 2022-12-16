package com.example.soeco.Models.API_Models

class Product(var id :String, var count:Int) {
    override fun toString(): String{
        return "$count $id"
    }
}