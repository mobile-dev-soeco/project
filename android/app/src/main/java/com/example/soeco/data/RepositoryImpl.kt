package com.example.soeco.data

import com.example.soeco.data.Models.DB_Models.*
import io.realm.RealmResults
import com.example.soeco.data.Models.CustomData
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Models.mongo.Deviation
import com.example.soeco.data.Models.mongo.TimeReport
import io.realm.mongodb.AppException
import io.realm.mongodb.User

class RepositoryImpl(
    private val realmDataSource: RealmDataSource
): Repository {

    override fun registerUser(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        userType: String,
        registerSuccess: () -> Unit,
        registerError: (Exception?) -> Unit
    ) {
        realmDataSource.registerUser(firstname, lastname, email, password, userType, registerSuccess, registerError)
    }

    override fun confirmUser(
        token: String,
        tokenId: String,
        confirmSuccess: () -> Unit,
        confirmError: (Exception) -> Unit
    ) {
        realmDataSource.confirmUser(token, tokenId, confirmSuccess, confirmError)
    }

    override fun updateUser(
        email: String,
        firstname: String,
        lastName: String,
        role: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        realmDataSource.updateUser(email, firstname, lastName, role, onSuccess, onError)
    }

    override fun resendConfirmationEmail(
        email: String,
        sendSuccess: () -> Unit,
        sendError: (Exception) -> Unit
    ) {
        realmDataSource.resendConfirmationEmail(email, sendSuccess, sendError)
    }

    override fun resetPassword(
        token: String,
        tokenId: String,
        newPassword: String,
        resetSuccess: () -> Unit,
        resetError: (Throwable?) -> Unit
    ) {
        realmDataSource.resetPassword(token, tokenId, newPassword, resetSuccess, resetError)
    }

    override fun sendPasswordResetEmail(
        email: String,
        sendSuccess: () -> Unit,
        sendError: (AppException?) -> Unit
    ) {
        realmDataSource.sendPasswordResetEmail(email, sendSuccess, sendError)
    }

    override fun login(
        email: String,
        password: String,
        loginSuccess: (User) -> Unit,
        loginError: (AppException?) -> Unit
    ) {
        realmDataSource.login(email, password, loginSuccess, loginError)
    }

    override fun logout(logoutSuccess: () -> Unit, logoutError: (Throwable?) -> Unit) {
        realmDataSource.logOut(logoutSuccess, logoutError)
    }

    override fun isUserLoggedIn(): Boolean = realmDataSource.isUserLoggedIn()

    override fun getUserRole() = realmDataSource.getUserRole()

    override fun restoreLoggedInUser() = realmDataSource.restoreLoggedInUser()

    override fun getOrder(id:String): Order_DB? {
        return realmDataSource.getOrder(id)
    }

    override fun getOrders() = realmDataSource.orders

    override fun get_Deviation_Reports() = realmDataSource.deviationReports
    override fun get_Tradesmen_Report() = realmDataSource.tradesmenReport
    override fun get_Material_Reports() = realmDataSource.materialReports
    override fun get_Delivery_Reports() = realmDataSource.deliveryReport
    override fun updateOrders() {
        realmDataSource.updateOrders()
    }

    override fun updateMaterials() {
        realmDataSource.updateMaterials()
    }

    override fun getMaterials() = realmDataSource.materials

    override fun updateProducts(orderNumber: String){
        return realmDataSource.updateProducts(orderNumber)
    }
    override fun getProductsDb(): RealmResults<Product_DB>{
        return realmDataSource.getProductsDb()
    }

    override fun getUsers(
        onSuccess: (users: MutableList<CustomData>) -> Unit,
        onError: (Exception) -> Unit)
    {
        realmDataSource.getUsers(onSuccess, onError)
    }

    override fun addDeviation(deviation : Deviation_Report_DB) {
        realmDataSource.addDeviation(deviation)
    }

    override fun deleteUser(user: CustomData, onSuccess: (id: String) -> Unit, onError: (Exception) -> Unit) {
        realmDataSource.deleteUser(user, onSuccess, onError)
    }

    override fun addMaterialReport(material: Material_Report_DB){
        realmDataSource.addMaterialReport(material)
    }

    override fun addTradesmenReport(tradesmenReportDb: Tradesmen_Report_DB){
        realmDataSource.addTradesmenReport(tradesmenReportDb)
    }

    override fun addDeliveryReport(delivery : Delivery_Report_DB) {
        realmDataSource.addDeliveryReport(delivery)

    }
    override fun clearLocaleDb(){
        realmDataSource.clearLocaleDb()
    }

    override fun getExpectedTime(orderNumber: String): String {
       return realmDataSource.getExpectedTime(orderNumber)
    }

    /** MongoDB methods **/

    override fun insertDeviation(
        deviation: Deviation,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        realmDataSource.insertDeviation(deviation, onSuccess, onError)
    }

    override fun getDeviations(id: String, onSuccess: (List<Deviation>) -> Unit, onError: (Exception) -> Unit) {
        realmDataSource.getDeviations(id, onSuccess, onError)
    }

    override fun insertTimeReport(
        report: TimeReport,
        onSuccess: (List<TimeReport>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun getTimeReports(
        id: String,
        onSuccess: (List<TimeReport>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        realmDataSource.getTimeReports(id, onSuccess, onError)
    }


}