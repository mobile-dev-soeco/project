package com.example.soeco.data

// MongoDB Service Packages

import android.content.Context
import android.util.Base64
import android.util.Log
import com.example.soeco.TAG
import com.example.soeco.data.Api.RetrofitClient
import com.example.soeco.data.Models.API_Models.Contact_API
import com.example.soeco.data.Models.API_Models.Material_API
import com.example.soeco.data.Models.API_Models.Order_API
import com.example.soeco.data.Models.API_Models.Product_API
import com.example.soeco.data.Models.CustomData
import com.example.soeco.data.Models.DB_Models.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.mongodb.*
import io.realm.mongodb.mongo.MongoClient
import io.realm.mongodb.mongo.MongoCollection
import io.realm.mongodb.mongo.MongoDatabase
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import retrofit2.Call
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.*

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
    lateinit var tradesmenReport: RealmResults<Tradesmen_Report_DB>
    lateinit var deliveryReport: RealmResults<Delivery_Report_DB>

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

        val tradesmen_Report_Query = localRealm.where(Tradesmen_Report_DB::class.java)
        val Material_Reports_Query = localRealm.where(Material_Report_DB::class.java)
        val Deviation_Reports_Query = localRealm.where(Deviation_Report_DB::class.java)
        val Delivery_Reports_Query = localRealm.where(Delivery_Report_DB::class.java)

        materials = materialQuery.findAllAsync()
        products = productQuery.findAllAsync()
        orders = orderQuery.findAllAsync()

        deviationReports = Deviation_Reports_Query.findAllAsync()
        materialReports = Material_Reports_Query.findAllAsync()
        tradesmenReport = tradesmen_Report_Query.findAllAsync()
        deliveryReport = Delivery_Reports_Query.findAllAsync()


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





    fun getUsers(
        onSuccess: (MutableList<CustomData>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val usersCollection = getUsersCollectionHandle()

        // Don't return the current user in user list
        val queryFilter = Document(
            "realmUserId", Document("\$ne", currentRealmUser.id)
        )

        usersCollection.find(queryFilter).iterator()
            .getAsync { task ->
                if (task.isSuccess) {
                    val users = mutableListOf<CustomData>()
                    val result = task.get()
                    while (result.hasNext()) {
                        val user = result.next()
                        users.add(user)
                    }
                    onSuccess.invoke(users)
                } else {
                    onError.invoke(task.error)
                }
            }
    }

    fun registerUser(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        userType: String,
        registerSuccess: () -> Unit,
        registerError: (Exception) -> Unit
    ) {

        val userData = listOf(firstname, lastname, email, userType)
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
                Log.e("User registration", "Database registration failed. Error: ${addUser.error.message}")
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

    fun updateUser(
        email: String,
        firstname: String,
        lastname: String,
        role: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val mongoClient: MongoClient = currentRealmUser.getMongoClient("mongodb-atlas")
        val mongoDatabase: MongoDatabase = mongoClient.getDatabase("auth")

        // Handle Plain Old Javascript Objects POJOs
        val pojoCodecRegistry = CodecRegistries.fromRegistries(
            AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
            CodecRegistries.fromProviders(
                PojoCodecProvider.builder().automatic(true).build()
            )
        )

        val users = mongoDatabase.getCollection(
            "users",
            Unit::class.java).withCodecRegistry(pojoCodecRegistry)
        Log.v(TAG(), "MongoDB collection collection handle instantiated")

        val queryFilter: Document = Document(
            "email", Document("\$eq", email)
        )

        val updates = Document(
                "\$set", mapOf(
                    "firstname" to firstname,
                    "lastname" to lastname,
                    "role" to role
                )
        )

        users.updateOne(queryFilter, updates)
            .getAsync { task ->
                if (task.isSuccess) {
                    onSuccess.invoke()
                } else {
                    onError.invoke(task.error)
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

    fun deleteUser(user: CustomData, onSuccess: (email: String) -> Unit, onError: (Exception) -> Unit){
        Log.v(TAG(), "Delete user with id ${user.id} called")
        realmApp.getFunctions(currentRealmUser).callFunctionAsync(
            "deleteUser",
            listOf(user.realmUserId, user.email),
            String::class.java
        ){
            if (it.isSuccess) {
                onSuccess.invoke(it.get())
            }
            else {
                onError.invoke(it.error)
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
        clearLocaleDb()
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
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR).toString()
                val month = calendar.get(Calendar.MONTH).toString()
                val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
                val dateString= "$year-$month-$day"
                val fullUrl = when (userRole.lowercase()){
                    "leverans" -> "orders/delivery/$dateString"
                    "snickare" -> "orders/blacksmith"
                    else -> "orders/carpenter"
                }

                val call: Call<ArrayList<Order_API>> = api.getOrder(getAuthToken(),fullUrl)
                val response = call.execute()
                if (response.isSuccessful) {
                    val orders = response.body()
                    if (orders != null) {
                        for (order in orders) {
                            it.copyToRealmOrUpdate(convertOrder(order))
                            Log.e("order", order.toString())

                        }
                    }
                } else {
                    Log.e("Repository:  Update:   if", response.toString())
                }
            } catch (error: IOException) {
                Log.e("Repository:  Update:  Catch", error.toString())
            }
        }
    }

    fun updateProducts(orderNumber: String) {
        val order = getOrder(orderNumber)?.idoo
        localRealm.executeTransactionAsync {
            try {
                val fullUrl = "products/"+ order.toString()

                val api = RetrofitClient.getInstance().api
                val call: Call<ArrayList<Product_API>> = api.getProducts(getAuthToken(),fullUrl)
                val response = call.execute()
                if (response.isSuccessful) {
                    val products = response.body()
                    if (products != null) {
                        it.delete(Product_DB::class.java)
                        for (product in products)
                            it.copyToRealmOrUpdate(convertProduct(product))
                    }
                } else {
                    Log.e("Repository:  getProduct:   if", response.toString())
                }
            } catch (error: IOException) {
                Log.e("Repository:  getProduct:  Catch", error.toString())

            }

        }
    }
    private fun convertOrder(fromApi: Order_API): Order_DB {

        val contact = getContact(fromApi.idoo)


        val item = Order_DB(fromApi.oo_nr, fromApi.idoo,0, contact?.phone , contact?.name)
        return item
    }

    private fun getContact(idoo : Int): Contact_API? {
        var contact :Contact_API? = null
        val fullUrl = "contact$idoo"
        val api = RetrofitClient.getInstance().api
        val call: Call<Contact_API> = api.getContact(getAuthToken(), fullUrl)
        val response = call.execute()
        if (response.isSuccessful) {
            contact = response.body()
        }
        return contact
    }
    private fun  convertProduct (fromApi: Product_API): Product_DB {

        val item = Product_DB(fromApi.id, fromApi.name, fromApi.OrderNumber, fromApi.count)
        return(item)
    }

    fun updateMaterials() {
        localRealm.executeTransactionAsync {
            try {
                val fullUrl = when (userRole.lowercase()) {
                    "snickare" -> "mateial/blacksmith"
                    else -> "mateial/carpenter"
                }
                val api = RetrofitClient.getInstance().api
                val call: Call<ArrayList<Material_API>> = api.getMaterial(getAuthToken(),fullUrl)
                val response = call.execute()
                if (response.isSuccessful) {
                    val materials = response.body()
                    if (materials != null) {
                        for (material in materials) {
                            Log.e("tag",material.toString())
                            it.copyToRealmOrUpdate(convertMaterial(material))
                        }
                    }
                }
            } catch (error: IOException) {
                Log.e("Repository:  getMaterial:  Catch", "Network Error")
            }
        }
    }

    private fun convertMaterial(fromApi: Material_API): Material_DB {
        val item = Material_DB(fromApi.id, fromApi.name, fromApi.unit)
        return(item)
    }


    fun getProductsDb(): RealmResults<Product_DB> {
        return localRealm.where(Product_DB::class.java).findAll()
    }

    private fun getUsersCollectionHandle(): MongoCollection<CustomData> {
        val mongoClient: MongoClient = currentRealmUser.getMongoClient("mongodb-atlas")
        val mongoDatabase: MongoDatabase = mongoClient.getDatabase("auth")

        // Handle Plain Old Javascript Objects POJOs
        val pojoCodecRegistry = CodecRegistries.fromRegistries(
            AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
            CodecRegistries.fromProviders(
                PojoCodecProvider.builder().automatic(true).build()
            )
        )

        val collection = mongoDatabase.getCollection(
            "users",
            CustomData::class.java).withCodecRegistry(pojoCodecRegistry)
        Log.v(TAG(), "MongoDB collection collection handle instantiated")

        return collection
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

    fun addTradesmenReport(tradesmenReportDb: Tradesmen_Report_DB) {
        localRealm.executeTransactionAsync {
            it.insert(tradesmenReportDb)
        }
    }

    fun addDeliveryReport(delivery: Delivery_Report_DB) {
        localRealm.executeTransactionAsync {
            it.insert(delivery)
        }
    }

    fun clearLocaleDb() {
        localRealm.executeTransactionAsync {
            it.delete(Order_DB::class.java)
            it.delete(Material_DB::class.java)
            it.delete(Product_DB::class.java)
        }
    }

    fun getExpectedTime(orderNumber: String): String {
        val order = getOrder(orderNumber)
        return order?.expectHours.toString()

    }

    fun getAuthToken(): String {
        var data = ByteArray(0)
        try {
            data = ("Soeco API" + ":" + "phefr3p2lGawlfrEkeprUrAs").toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP)
    }
}