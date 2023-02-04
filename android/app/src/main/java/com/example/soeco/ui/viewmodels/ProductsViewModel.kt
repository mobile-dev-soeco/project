package com.example.soeco.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Repository
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Models.DB_Models.Tradesmen_Report_DB
import io.realm.RealmResults

class ProductsViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    fun getExpectedTime(orderNumber:String): String {
        return  repository.getExpectedTime(orderNumber)
    }

    fun addProductReport(tradesmenReportDb: Tradesmen_Report_DB) {
            repository.addTradesmenReport(tradesmenReportDb)
    }

}