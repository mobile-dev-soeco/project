package com.example.soeco.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Models.DB_Models.Material_DB
import com.example.soeco.data.Repository
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Models.DB_Models.Product_Report_DB
import io.realm.RealmList
import io.realm.RealmResults

class ProductsViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {


    fun getProducts(): RealmResults<Product_DB> {
        return  repository.getProductsDb()

    }

    fun addProductReport(productReportDb: Product_Report_DB) {
            repository.addProductReport(productReportDb)
    }

}