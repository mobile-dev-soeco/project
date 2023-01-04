package com.example.soeco.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Models.DB_Models.Deviation_Report_DB
import com.example.soeco.data.Models.DB_Models.Material_Report_DB
import com.example.soeco.data.Repository
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.data.Models.DB_Models.Product_Report_DB
import io.realm.RealmResults

class StatsViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    val deviationReports: RealmResults<Deviation_Report_DB> = repository.get_Deviation_Reports()
    val productReport: RealmResults<Product_Report_DB> = repository.get_Product_Report()
    val materialReportDb: RealmResults<Material_Report_DB> = repository.get_Material_Reports()


}