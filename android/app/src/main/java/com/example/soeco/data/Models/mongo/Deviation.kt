package com.example.soeco.data.Models.mongo

import org.bson.types.ObjectId

data class Deviation (
    var _id: ObjectId = ObjectId(),
    var owner_id: String = "",
    var date: String = "",
    var time: String = "",
    var problem: String ="",
    var solution: String ="",
    var cost: String = ""
)