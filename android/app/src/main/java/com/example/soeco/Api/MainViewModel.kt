package com.example.soeco.Api

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.Models.DB_Models.Product_DB
import com.example.soeco.realmAppServices
import io.realm.RealmQuery
import io.realm.RealmResults

class MainViewModel (application: Application) : AndroidViewModel(application) {
    private val user = realmAppServices.currentUser()

    private val repository: Repository = Repository(application)
    val orders: RealmResults<Order_DB> = repository.orders
    val materials: RealmResults<Material_DB> = repository.materials
    val products: RealmResults<Product_DB> = repository.products


    fun getOrder(id : String): Order_DB? {
        return repository.getOrder(id)
    }

    fun update() {
        var userRole=
            if (user!= null)  user.customData["role"].toString()
            else "delivery"

        repository.updateOrders(userRole)
        repository.updateMaterials(userRole)


    }

}