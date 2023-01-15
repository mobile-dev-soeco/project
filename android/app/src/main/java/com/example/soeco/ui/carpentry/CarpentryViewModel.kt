package com.example.soeco.ui.carpentry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Models.mongo.TimeReport
import com.example.soeco.data.Repository
import io.realm.RealmResults
import org.bson.types.ObjectId
import java.util.*

class CarpentryViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _timeReportLiveData = MutableLiveData<List<TimeReport>>()
    val timeReportLiveData: LiveData<List<TimeReport>>
        get() = _timeReportLiveData

    private val _currentProduct = MutableLiveData<Product_DB>()
    val currentProduct: LiveData<Product_DB>
        get() = _currentProduct

    fun setCurrentProduct(product: Product_DB){
        _currentProduct.value = product
    }

    fun getProducts(): RealmResults<Product_DB> {
        return repository.getProductsDb()
    }

    fun getTimeReports(id: String) {
        repository.getUsersTimeReports(
            id,
            onSuccess = { reports ->
                _timeReportLiveData.value = reports
            },
            onError = {
                Log.e(TAG(), it.toString())
            }
        )
    }

    fun insertTimeReport(id: String) {
        val report = TimeReport(
            _id = ObjectId(),
            ownerId = id,
            userId = repository.getUserId(),
            userRole = repository.getUserRole(),
            date = Date(),
            hours = 1.5f
        )
        repository.insertTimeReport(
            report,
            onSuccess = {
                Log.v(TAG(), "Time report added")
                getTimeReports(id)
            },
            onError = {
                Log.e(TAG(), it.toString())
            }
        )
    }

    fun clearTimeReports() {
        _timeReportLiveData.value = mutableListOf()
    }

}