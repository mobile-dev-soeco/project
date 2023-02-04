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
import com.example.soeco.utils.ActionResult
import io.realm.RealmResults
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

    private val _insertReportResult = MutableLiveData<ActionResult>()
    val insertReportResult: LiveData<ActionResult>
        get() = _insertReportResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

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
            reportId = UUID.randomUUID().toString(),
            ownerId = productId,
            userId = repository.getUserId(),
            userRole = repository.getUserRole(),
            date = date,
            hours = formatHours(hours, minutes)
        )

        repository.insertTimeReport(
            report,
            onSuccess = {
                _insertReportResult.value = ActionResult.Success
                _isLoading.value = false
            },
            onError = {
                _insertReportResult.value = ActionResult.Error
                _isLoading.value = false
            }
        )
        _isLoading.value = true
    }

    fun deleteTimeReport(reportId: String) {
        repository.deleteTimeReport(
            reportId,
            onSuccess = {
//                _currentProduct.value?.product_id?.let { getTimeReports(it) }
            },
            onError = {
                // TODO: Handle error
            }
        )
    }

    fun clearTimeReports() {
        _timeReportLiveData.value = mutableListOf()
    }

    fun clearResult() {
        _insertReportResult.value = ActionResult.Handled
    }

    private fun formatHours(hours: Int, minutes: Int): Float {
        val time = hours.toDouble() + (minutes.toDouble()/MINUTES_PER_HOUR)
        return BigDecimal(time).setScale(2, RoundingMode.CEILING).toFloat()
    }

}