package com.example.soeco.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Models.DB_Models.Deviation_Report_DB
import com.example.soeco.data.Models.DB_Models.Material_DB
import com.example.soeco.data.Models.DB_Models.Material_Report_DB
import com.example.soeco.data.Repository
import com.example.soeco.data.Models.DB_Models.Order_DB
import io.realm.RealmResults

class MaterialsViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    val materials: RealmResults<Material_DB> = repository.getMaterials()

    fun addMaterialReport(material: Material_Report_DB ){
        repository.addMaterialReport(material)
    }

}