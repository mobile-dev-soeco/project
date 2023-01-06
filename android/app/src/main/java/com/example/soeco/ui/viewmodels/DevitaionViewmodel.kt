package com.example.soeco.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Models.DB_Models.Deviation_Report_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Repository
import io.realm.RealmResults

class DevitaionViewmodel (
        val repository: Repository,
        val savedStateHandle: SavedStateHandle
    ): ViewModel() {

        fun getProducts(): RealmResults<Product_DB> {
            return  repository.getProductsDb()

        }

        fun addDeviation(deviation : Deviation_Report_DB){
            repository.addDeviation(deviation)
        }

    }


