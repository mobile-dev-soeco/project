package com.example.soeco.data.Models.mongo

import org.bson.types.ObjectId
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class TimeReport (
    var _id: ObjectId = ObjectId(),
    var ownerId: String = "",
    var userId: String = "",
    var userRole: String = "",
    var date: Date = Date(),
    var hours: Float = 0f
)