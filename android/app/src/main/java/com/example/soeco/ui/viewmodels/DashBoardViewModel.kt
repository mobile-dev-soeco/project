package com.example.soeco.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Repository
import com.example.soeco.data.Models.DB_Models.Order_DB
import io.realm.RealmResults
import java.time.LocalDateTime

class DashBoardViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    val orders: RealmResults<Order_DB> = repository.getOrders()
    val userRole: String = repository.getUserRole()
    var date: LocalDateTime= LocalDateTime.now()
    init {
        updateDB()
    }
    fun prevDay(){
        date = date.minusDays(1)
    }
    fun nextDay(){
        date = date.plusDays(1)
    }
    fun updateDB(){
        clearLocaleDb()
        repository.updateMaterials()
        repository.updateOrders(date)
    }

    fun clearLocaleDb(){
        repository.clearLocaleDb()
    }
}