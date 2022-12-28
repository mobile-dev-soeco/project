package com.example.soeco.data

import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.Models.DB_Models.Product_DB
import io.realm.RealmResults
import io.realm.mongodb.App
import io.realm.mongodb.AppException
import io.realm.mongodb.User
import io.realm.mongodb.functions.Functions

interface Repository {

    fun registerUser(
        email: String,
        password: String,
        userType: String,
        registerSuccess: () -> Unit,
        registerError: (Exception?) -> Unit
    )

    fun confirmUser(
        token: String,
        tokenId: String,
        confirmSuccess: () -> Unit,
        confirmError: (Exception) -> Unit
    )

    fun resendConfirmationEmail(
        email: String,
        sendSuccess: () -> Unit,
        sendError: (Exception) -> Unit
    )

    fun resetPassword(
        token: String,
        tokenId: String,
        newPassword: String,
        resetSuccess: () -> Unit,
        resetError: (Throwable?) -> Unit
    )

    fun sendPasswordResetEmail(
        email: String,
        sendSuccess: () -> Unit,
        sendError: (AppException?) -> Unit
    )

    fun login(
        email: String,
        password: String,
        loginSuccess: (User) -> Unit,
        loginError: (AppException?) -> Unit
    )

    fun isUserLoggedIn(): Boolean

    fun logout(logoutSuccess: () -> Unit, logoutError: (Throwable?) -> Unit)

    fun getUserRole(): String

    fun restoreLoggedInUser(): User?

    fun getOrder(id: String): Order_DB?

    fun getOrders(): RealmResults<Order_DB>

    fun updateOrders(): Unit

    fun updateMaterials(): Unit

    fun getMaterials(): List<Material_DB>

    fun getProducts(): List<Product_DB>

}