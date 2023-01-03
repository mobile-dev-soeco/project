package com.example.soeco.data

import com.example.soeco.data.Models.DB_Models.*
import io.realm.RealmList
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

    fun getMaterials(): RealmResults<Material_DB>

    fun getProducts(): RealmResults<Product_DB>

    fun getProduct(id:String): Product_DB?

    fun getProductsDb(orderNumber: String): RealmResults<Product_DB>

    fun addDeviation(deviation : Deviation_Report_DB)
    fun addMaterialReport(material: Material_Report_DB)
    fun addProductReport(productReportDb: Product_Report_DB)
}