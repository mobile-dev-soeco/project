package com.example.soeco.data.Models.DB_Models


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required




open class Material_DB(
    @PrimaryKey
    var id: Int = 0,
    @Required
    var name: String = "",
    var unit: String = "",
    ) : RealmObject()