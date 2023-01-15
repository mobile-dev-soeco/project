package com.example.soeco.ui.carpentry

import android.util.Log
import android.widget.Toast
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
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class CarpentryViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val MINUTES_PER_HOUR = 60

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

    fun insertTimeReport(date: Date, hours: Int, minutes: Int) {

        val productId = _currentProduct.value?.product_id.toString()

        val report = TimeReport(
            _id = ObjectId(),
            ownerId = productId,
            userId = repository.getUserId(),
            userRole = repository.getUserRole(),
            date = date,
            hours = formatHours(hours, minutes)
        )

        repository.insertTimeReport(
            report,
            onSuccess = {
                Log.v(TAG(), "Time report added")
            },
            onError = {
                Log.e(TAG(), it.toString())
            }
        )
    }

    fun deleteTimeReport(id: ObjectId) {
        repository.deleteTimeReport(
            id,
            onSuccess = {
                _currentProduct.value?.product_id?.let { getTimeReports(it) }
            },
            onError = {

            }
        )
    }

    fun clearTimeReports() {
        _timeReportLiveData.value = mutableListOf()
    }

    private fun formatHours(hours: Int, minutes: Int): Float {
        var time: Double = hours.toDouble() + (minutes.toDouble()/MINUTES_PER_HOUR)
        var timeBd = BigDecimal(time)
        return timeBd.setScale(2, RoundingMode.HALF_UP).toFloat()
    }

}