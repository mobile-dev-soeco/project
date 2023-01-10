package com.example.soeco.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Models.DB_Models.*
import com.example.soeco.data.Repository
import io.realm.RealmList
import io.realm.RealmResults

class DeliveryProductsViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {


    fun getProducts(): RealmResults<Product_DB> {
        return  repository.getProductsDb()

    }

    fun getExpectedTime(orderNumber:String): String {
        return  repository.getExpectedTime(orderNumber)
    }

    fun addDeliveryReport(report: Delivery_Report_DB) {
            repository.addDeliveryReport(report)
    }

}