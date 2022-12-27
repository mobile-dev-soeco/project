package com.example.soeco.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Repository
import com.example.soeco.Models.DB_Models.Order_DB
import io.realm.RealmResults

class DashBoardViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    val orders: RealmResults<Order_DB> = repository.getOrders()
    val userRole: String = repository.getUserRole()

    init {
        repository.updateOrders()
    }

}