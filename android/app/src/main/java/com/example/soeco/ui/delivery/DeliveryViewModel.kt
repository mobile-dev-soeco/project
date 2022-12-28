package com.example.soeco.ui.delivery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.data.Repository
import io.realm.RealmResults

class DeliveryViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _activeOrder: String? = null
    val orders: RealmResults<Order_DB> = repository.getOrders()

    init {
        repository.updateOrders()
    }

    fun setActiveOrder(value: String) {
        _activeOrder = value
    }

    fun getOrder(): Order_DB? {
        return _activeOrder?.let { repository.getOrder(it) }
    }

}