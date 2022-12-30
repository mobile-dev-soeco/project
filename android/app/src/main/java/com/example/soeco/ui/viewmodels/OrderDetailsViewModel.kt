package com.example.soeco.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.data.Repository
import io.realm.RealmResults

class OrderDetailsViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {


    fun getOrder(order : String): Order_DB? {
        return  repository.getOrder(order)
    }

}