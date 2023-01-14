package com.example.soeco.ui.carpentry.products

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Models.mongo.TimeReport
import com.example.soeco.data.Repository
import org.bson.types.ObjectId
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class ProductDetailsViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _timeReportLiveData = MutableLiveData<List<TimeReport>>()
    val timeReportLiveData: LiveData<List<TimeReport>>
        get() = _timeReportLiveData

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

}