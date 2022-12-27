package com.example.soeco.Api

import androidx.lifecycle.*
import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.Models.DB_Models.Product_DB
import io.realm.RealmResults

class MainViewModel () : ViewModel() {
//    private val user = realmAppServices.currentUser()

    private val repository: Repository = Repository()
    val orders: RealmResults<Order_DB> = repository.orders
    val materials: RealmResults<Material_DB> = repository.materials
    val products: RealmResults<Product_DB> = repository.products
//    var userRole :String? = user?.customData?.get("role").toString()
    private var activeOrder :String? = null

    fun getOrder(): Order_DB? {
        return activeOrder?.let { repository.getOrder(it) }
    }

    fun setActiveOrder(order :String){
        activeOrder = order
    }


//    fun update() {
//        userRole?.let { repository.updateOrders(it) }
//        userRole?.let { repository.updateMaterials(it) }
//    }

}