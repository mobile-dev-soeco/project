package com.example.soeco.data.Models.mongo

import org.bson.types.ObjectId
import java.time.LocalDate

data class TimeReport (
    var _id: ObjectId = ObjectId(),
    var owner_id: String = "",
    var date: LocalDate = LocalDate.now(),
    var hours: Float = 0f
)