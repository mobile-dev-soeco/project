package com.example.soeco.data

import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.Models.DB_Models.Product_DB
import io.realm.mongodb.AppException
import io.realm.mongodb.User

interface Repository {

    fun login(
        email: String,
        password: String,
        loginSuccess: (User) -> Unit,
        loginError: (AppException?) -> Unit
    )

    fun logout(logoutSuccess: () -> Unit, logoutError: (Throwable?) -> Unit)

    fun getUserRole(): String

    fun restoreLoggedInUser(): User?

    fun getOrder(id: String): Order_DB?

    fun getOrders(): List<Order_DB>

    fun updateOrders(): Unit

    fun updateMaterials(): Unit

    fun getMaterials(): List<Material_DB>

    fun getProducts(): List<Product_DB>

}