package com.example.soeco.data

import android.util.Log
import com.example.soeco.Api.RetrofitClient
import com.example.soeco.Models.API_Models.Material_API
import com.example.soeco.Models.API_Models.Order_API
import com.example.soeco.Models.API_Models.Product_API
import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.Models.DB_Models.Product_DB
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.mongodb.AppException
import io.realm.mongodb.User
import io.realm.mongodb.functions.Functions
import retrofit2.Call
import java.io.IOException

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

    override fun getUserRole() = realmDataSource.getUserRole()

    override fun restoreLoggedInUser() = realmDataSource.restoreLoggedInUser()

    override fun getOrder(id:String): Order_DB? {
        return realmDataSource.getOrder(id)
    }

    override fun getOrders() = realmDataSource.orders

    override fun updateOrders() {
        realmDataSource.updateOrders()
    }

    override fun updateMaterials() {
        realmDataSource.updateMaterials()
    }

    override fun getMaterials() = realmDataSource.materials

    override fun getProducts() = realmDataSource.products

}