package com.example.soeco.data.Models.mongo

import java.util.Date

data class TimeReport (
    var reportId: String = "",
    var ownerId: String = "",
    var userId: String = "",
    var userRole: String = "",
    var date: Date = Date(),
    var hours: Float = 0f
)