package com.example.soeco.data

import com.example.soeco.data.Models.DB_Models.*
import io.realm.RealmResults
import io.realm.mongodb.AppException
import io.realm.mongodb.User

class RepositoryImpl(
    private val realmDataSource: RealmDataSource
): Repository {

    override fun registerUser(
        email: String,
        password: String,
        userType: String,
        registerSuccess: () -> Unit,
        registerError: (Exception?) -> Unit
    ) {
        realmDataSource.registerUser(email, password, userType, registerSuccess, registerError)
    }

    override fun confirmUser(
        token: String,
        tokenId: String,
        confirmSuccess: () -> Unit,
        confirmError: (Exception) -> Unit
    ) {
        realmDataSource.confirmUser(token, tokenId, confirmSuccess, confirmError)
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
    override fun get_Product_Report() = realmDataSource.productReport
    override fun get_Material_Reports() = realmDataSource.materialReports

    override fun updateOrders() {
        realmDataSource.updateOrders()
    }

    override fun updateMaterials() {
        realmDataSource.updateMaterials()
    }

    override fun getMaterials() = realmDataSource.materials

    override fun getProducts() = realmDataSource.products

    override fun getProduct(id:String) : Product_DB?{
        return realmDataSource.getProductRealm(id)
    }

    override fun getProductsDb(orderNumber: String): RealmResults<Product_DB>{
        return realmDataSource.getProductsDb(orderNumber)

    }

    override fun addDeviation(deviation : Deviation_Report_DB){
        realmDataSource.addDeviation(deviation)
    }

    override fun addMaterialReport(material: Material_Report_DB){
        realmDataSource.addMaterialReport(material)

    }
    override fun addProductReport(productReportDb: Product_Report_DB){
        realmDataSource.addProductReport(productReportDb)

    }

}