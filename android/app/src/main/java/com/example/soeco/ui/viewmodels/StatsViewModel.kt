package com.example.soeco.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Models.DB_Models.*
import com.example.soeco.data.Repository
import io.realm.RealmResults

class StatsViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    val deviationReports: RealmResults<Deviation_Report_DB> = repository.get_Deviation_Reports()
    val tradesmenReports: RealmResults<Tradesmen_Report_DB> = repository.get_Tradesmen_Report()
    val materialReports: RealmResults<Material_Report_DB> = repository.get_Material_Reports()
    val deliveryReports: RealmResults<Delivery_Report_DB> = repository.get_Delivery_Reports()





}