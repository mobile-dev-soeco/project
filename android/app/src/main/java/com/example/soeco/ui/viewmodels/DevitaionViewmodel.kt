package com.example.soeco.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Models.DB_Models.Deviation_Report_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Models.mongo.Deviation
import com.example.soeco.data.Repository
import io.realm.RealmResults

class DevitaionViewmodel (
        val repository: Repository,
        val savedStateHandle: SavedStateHandle
    ): ViewModel() {

        fun getProducts(orderNumber: String): RealmResults<Product_DB> {
            return  repository.getProductsDb(orderNumber)
        }

        fun addDeviation(deviation : Deviation_Report_DB){
            repository.addDeviation(deviation)
        }

        fun insertDeviation(deviation: Deviation){
            repository.insertDeviation(
                deviation,
                onSuccess = {
                    Log.v(TAG(), "On success invoked for insertDeviation")
                },
                onError = {
                    Log.e(TAG(), it.message.toString())
                }
            )
        }

    }


