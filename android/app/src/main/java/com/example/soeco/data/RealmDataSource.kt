package com.example.soeco.data

import android.content.Context
import android.util.Log
import com.example.soeco.data.Api.RetrofitClient
import com.example.soeco.data.Models.API_Models.Material_API
import com.example.soeco.data.Models.API_Models.Order_API
import com.example.soeco.data.Models.API_Models.Product_API
import com.example.soeco.data.Models.DB_Models.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.AppException
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import retrofit2.Call
import java.io.IOException

class RealmDataSource(context: Context) {

    private val realmApp: App
    private val APP_ID = "soecoapp-ciaaa"

    private lateinit var localRealm: Realm
    private lateinit var currentRealmUser: User
    private lateinit var userRole: String
    lateinit var materials: RealmResults<Material_DB>
    lateinit var orders: RealmResults<Order_DB>
    lateinit var products: RealmResults<Product_DB>
    lateinit var deviationReports: RealmResults<Deviation_Report_DB>
    lateinit var materialReports: RealmResults<Material_Report_DB>
    lateinit var productReport: RealmResults<Product_Report_DB>

    init {
        Log.v("Realm Data", "Created a new instance of Realm data source")
        Realm.init(context)
        realmApp = App(AppConfiguration.Builder(APP_ID).build())
    }

    // Called when user logs in or is restored
    private fun instantiateRealm(user: User) {

        currentRealmUser = user
        userRole = currentRealmUser.customData["role"].toString()
        localRealm()
        val userData = user.customData
        val productQuery = localRealm.where(Product_DB::class.java)
        val orderQuery = localRealm.where(Order_DB::class.java)
        val materialQuery = localRealm.where(Material_DB::class.java)

        val Product_Report_Query = localRealm.where(Product_Report_DB::class.java)
        val Material_Reports_Query = localRealm.where(Material_Report_DB::class.java)
        val Deviation_Reports_Query = localRealm.where(Deviation_Report_DB::class.java)

        materials = materialQuery.findAllAsync()
        products = productQuery.findAllAsync()
        orders = orderQuery.findAllAsync()

        deviationReports = Deviation_Reports_Query.findAllAsync()
        materialReports = Material_Reports_Query.findAllAsync()
        productReport = Product_Report_Query.findAllAsync()

    }


    private fun localRealm(){
        val config = RealmConfiguration.Builder()
            .name("local-Realm")
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .build()

        localRealm = Realm.getInstance(config)
    }





    fun registerUser(
        email: String,
        password: String,
        userType: String,
        registerSuccess: () -> Unit,
        registerError: (Exception) -> Unit
    ) {

        val userData = listOf(email, password, userType)
        val functionsManager = realmApp.getFunctions(currentRealmUser)

        // Create user in database
        functionsManager.callFunctionAsync("registerUser", userData, String::class.java) { addUser ->
            if (addUser.isSuccess){
                Log.v("User registration", "User was added to database")
                // Register the user to localRealm email/password authentication
                realmApp.emailPassword.registerUserAsync(email, password) { registerUser ->
                    if (registerUser.isSuccess) {
                        Log.v("User registration", "User was added to emailPassword auth service.")
                        registerSuccess.invoke()
                    } else {
                        Log.e("User registration", "emailPassword registration failed. Error: ${registerUser.error.message}")
                        registerError.invoke(registerUser.error)
                        // User could not be added to localRealm authentication. Remove the user from the database.
                        functionsManager.callFunctionAsync("DeleteUser", null, Unit::class.java) { deleteUser ->
                            if (deleteUser.isSuccess) {
                                Log.v("User registration", "User was deleted from database")
                            } else {
                                Log.e("User registration", "User deletion failed. Error: ${deleteUser.error.message}")
                            }
                        }
                    }
                }
            } else {
                // User could not be added to database.
                Log.v("User registration", "Database registration failed. Error: ${addUser.error.message}")
                registerError.invoke(addUser.error)
            }
        }
    }

    fun confirmUser(
        token: String,
        tokenId: String,
        confirmSuccess: () -> Unit,
        confirmError: (Exception) -> Unit
    ) {
        realmApp.emailPassword.confirmUserAsync(token, tokenId) {
            if (it.error != null){
                confirmError.invoke(it.error)
            } else {
                confirmSuccess.invoke()
            }
        }
    }

    fun resendConfirmationEmail(
        email: String,
        sendSuccess: () -> Unit,
        sendError: (Exception) -> Unit
    ) {
        realmApp.emailPassword.resendConfirmationEmailAsync(email) {
            if(it.error != null) {
                sendError.invoke(it.error)
            } else {
                sendSuccess.invoke()
            }
        }
    }

    fun resetPassword(
        token: String,
        tokenId: String,
        newPassword: String,
        resetSuccess: () -> Unit,
        resetError: (Exception) -> Unit
    ) {
        realmApp.emailPassword.resetPasswordAsync(token, tokenId, newPassword){
            if (it.error != null){
                resetError.invoke(it.error)
            } else {
                resetSuccess.invoke()
            }
        }

    }

    fun sendPasswordResetEmail(
        email: String,
        sendSuccess: () -> Unit,
        sendError: (AppException?) -> Unit
    ) {
        realmApp.emailPassword.sendResetPasswordEmailAsync(email){
            if (it.error != null) {
                sendError.invoke(it.error)
            } else {
                sendSuccess.invoke()
            }
        }
    }

    fun login(
        email: String,
        password: String,
        loginSuccess: (User) -> Unit,
        loginError: (AppException?) -> Unit
    ) {
        val userCredentials = Credentials.emailPassword(email, password)
        realmApp.loginAsync(userCredentials) {
            if (it.error != null){
                loginError.invoke(it.error)
            } else {
                val user = it.get()
                instantiateRealm(user)
                loginSuccess.invoke(user)
            }
        }
    }

    fun logOut(
        logoutSuccess: () -> Unit,
        logoutError: (Throwable?) -> Unit
    ) {
        realmApp.currentUser()?.logOutAsync {
            if (it.error != null) {
                logoutError.invoke(it.error.exception)
            } else {
                logoutSuccess.invoke()
            }
        }
    }

    fun getUserRole(): String {
        return userRole
    }

    fun restoreLoggedInUser(): io.realm.mongodb.User? {
        return realmApp.currentUser()?.also {
            instantiateRealm(it)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return realmApp.currentUser()?.isLoggedIn ?: false
    }







    fun getOrder(id: String): Order_DB? {
        return localRealm.where(Order_DB::class.java).containsKey("_id", id).findFirst()
    }

    fun updateOrders() {
        localRealm.executeTransactionAsync {
            try {
                val api = RetrofitClient.getInstance().api
                val call: Call<ArrayList<Order_API>> = api.getOrder(userRole)
                val response = call.execute()
                if (response.isSuccessful) {
                    val orders = response.body()
                    if (orders != null) {
                        it.delete(Order_DB::class.java)
                        it.delete(Material_DB::class.java)
                        it.delete(Product_DB::class.java)

                        for (order in orders) {
                            for (product in order.Products)
                                it.copyToRealmOrUpdate(getProduct(product.id,order.OrderNumber,product.count))

                            it.copyToRealmOrUpdate(convertOrder(order))
                        }
                    }
                } else {
                    Log.e("Repository:  Update:   if", "Network Error")
                }
            } catch (error: IOException) {
                Log.e("Repository:  Update:  Catch", "Network Error")
            }
        }
    }

    private fun getProduct(id: Int, ordernumber: String,count: Int): Product_DB {
        try {
            val api = RetrofitClient.getInstance().api
            val call: Call<Product_API> = api.getProduct(id)
            val response = call.execute()
            if (response.isSuccessful) {
                val product = response.body()
                if (product != null) {
                    return(convertProduct(product,ordernumber,count))
                }
            } else {
                Log.e("Repository:  getProduct:   if", "Network Error")
            }
        } catch (error: IOException) {
            Log.e("Repository:  getProduct:  Catch", "Network Error")

        }
        return Product_DB("0","error","error")
    }

    private fun convertOrder(fromApi: Order_API): Order_DB {

        val contact_List = RealmList<String>()
        if(fromApi.contact != null) {
            for (string in fromApi.contact)
                contact_List.add(string)
        }

        val item = Order_DB(fromApi.OrderNumber, fromApi.expectHours, fromApi.address, contact_List)
        return item
    }

    private fun  convertProduct (fromApi: Product_API, ordernumber :String,count :Int): Product_DB {
        val item = Product_DB(fromApi.id.toString(), fromApi.name, ordernumber,count)
        return(item)
    }

    fun updateMaterials() {
        localRealm.executeTransactionAsync {
            try {
                val api = RetrofitClient.getInstance().api
                val call: Call<ArrayList<Material_API>> = api.getMaterial(userRole)
                val response = call.execute()
                if (response.isSuccessful) {
                    val materials = response.body()
                    if (materials != null) {
                        for (material in materials) {
                            it.copyToRealmOrUpdate(convertMaterial(material))
                        }
                    }
                }
            } catch (error: IOException) {
                Log.e("Repository:  getProduct:  Catch", "Network Error")
            }
        }
    }

    private fun convertMaterial(fromApi: Material_API): Material_DB {
        val item = Material_DB(fromApi.id, fromApi.name, fromApi.unit)
        return(item)
    }

    fun getProductRealm(id: String): Product_DB? {
        return localRealm.where(Product_DB::class.java).containsKey("id", id).findFirst()

    }

    fun getProductsDb(orderNumber: String): RealmResults<Product_DB> {
        return localRealm.where(Product_DB::class.java).containsKey("orderNumber",
            orderNumber).findAll()

    }
    fun addDeviation(deviation : Deviation_Report_DB) {
        localRealm.executeTransactionAsync {
            it.insert(deviation)
        }
    }
    fun addMaterialReport(material: Material_Report_DB) {
        localRealm.executeTransactionAsync {
            it.insert(material)
        }
    }

    fun addProductReport(productReportDb: Product_Report_DB) {
        localRealm.executeTransactionAsync {
            it.insert(productReportDb)
        }
    }

    fun addDeliveryReport(delivery: Delivery_Report_DB) {
        localRealm.executeTransactionAsync {
            it.insert(delivery)
        }
    }


}