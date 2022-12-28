package com.example.soeco.ui.carpentry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.data.Repository
import io.realm.RealmResults

class CarpentryDashBoardViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    val orders: RealmResults<Order_DB> = repository.getOrders()

    init {
        repository.updateOrders()
    }
}