package com.example.soeco.data

import com.example.soeco.data.Models.DB_Models.*
import com.example.soeco.data.Models.CustomData
import com.example.soeco.data.Models.DB_Models.Material_DB
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Models.mongo.Deviation
import com.example.soeco.data.Models.mongo.TimeReport
import io.realm.RealmResults
import io.realm.mongodb.AppException
import io.realm.mongodb.User
import java.time.LocalDateTime

interface Repository {

    fun registerUser(
        firstname: String,
        lastName: String,
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

    fun updateOrders(date: LocalDateTime): Unit

    fun updateMaterials(): Unit

    fun clearLocaleDb():Unit

    fun getMaterials(): RealmResults<Material_DB>



    fun getProductsDb(): RealmResults<Product_DB>

    fun addDeviation(deviation : Deviation_Report_DB)
    fun addMaterialReport(material: Material_Report_DB)
    fun addTradesmenReport(tradesmenReportDb: Tradesmen_Report_DB)
    fun get_Deviation_Reports() : RealmResults<Deviation_Report_DB>
    fun get_Tradesmen_Report()  : RealmResults<Tradesmen_Report_DB>
    fun get_Material_Reports()  : RealmResults<Material_Report_DB>
    fun get_Delivery_Reports(): RealmResults<Delivery_Report_DB>

    fun getUsers(
        onSuccess: (MutableList<CustomData>) -> Unit,
        onError: (Exception) -> Unit
    )

    fun updateUser(
        email: String,
        firstname: String,
        lastName: String,
        role: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun deleteUser(
        user: CustomData,
        onSuccess: (id: String) -> Unit,
        onError: (Exception) -> Unit
    )
    fun addDeliveryReport(delivery : Delivery_Report_DB) : Unit
    fun updateProducts(orderNumber: String)
    fun getExpectedTime(orderNumber: String) : String

    fun insertDeviation(deviation: Deviation, onSuccess: () -> Unit, onError: (Exception) -> Unit)

    fun getDeviations(id: String, onSuccess: (List<Deviation>) -> Unit, onError: (Exception) -> Unit)

    fun insertTimeReport(report: TimeReport, onSuccess: (List<TimeReport>) -> Unit, onError: (Exception) -> Unit)

    fun getTimeReports(id: String, onSuccess: (List<TimeReport>) -> Unit, onError: (Exception) -> Unit)
}