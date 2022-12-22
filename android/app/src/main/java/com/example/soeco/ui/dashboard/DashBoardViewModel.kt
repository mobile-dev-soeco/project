package com.example.soeco.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.data.Repository

class DashBoardViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _orders = MutableLiveData<List<Order_DB>>(repository.getOrders())
    val orders: LiveData<List<Order_DB>>
        get() = _orders

    private val _materials = MutableLiveData<List<Material_DB>>(repository.getMaterials())
    val materials: LiveData<List<Material_DB>>
        get() = _materials

    fun fetchData() {
        repository.updateOrders()
        repository.updateMaterials()
    }


}