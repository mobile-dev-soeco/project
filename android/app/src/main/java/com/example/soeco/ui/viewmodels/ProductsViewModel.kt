package com.example.soeco.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Models.DB_Models.Material_DB
import com.example.soeco.data.Repository
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import io.realm.RealmResults

class ProductsViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    val products: RealmResults<Product_DB> = repository.getProducts()

    fun getProduct(id : String): Product_DB? {
        return  repository.getProduct(id)
    }

}