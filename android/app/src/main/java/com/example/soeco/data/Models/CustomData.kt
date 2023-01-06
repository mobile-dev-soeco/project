package com.example.soeco.data.Models

import org.bson.types.ObjectId

data class CustomData (
    var id: ObjectId = ObjectId(),
    var email: String? = "",
    var role: String? = "",
    var realmUserId: String? = null,
    var firstname: String? = "",
    var lastname: String? = "",
        ) {
    override fun toString(): String {
        return "id: $id, name: \"$firstname $lastname\", email: $email, role: $role, realmUserId: $realmUserId"
    }
}