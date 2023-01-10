package com.example.soeco.data

import android.content.Context
import android.util.Log
import com.example.soeco.TAG
import com.example.soeco.data.Api.RetrofitClient
import com.example.soeco.data.Models.API_Models.Material_API
import com.example.soeco.data.Models.API_Models.Order_API
import com.example.soeco.data.Models.API_Models.Product_API
import com.example.soeco.data.Models.CustomData
import com.example.soeco.data.Models.DB_Models.Material_DB
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Models.DB_Models.*
import com.example.soeco.data.Models.mongo.Deviation
import com.example.soeco.data.Models.mongo.TimeReport
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.AppException
import io.realm.mongodb.Credentials
import io.realm.mongodb.User

// MongoDB Service Packages
import io.realm.mongodb.mongo.MongoClient
import io.realm.mongodb.mongo.MongoCollection
import io.realm.mongodb.mongo.MongoDatabase
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

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
        val usersCollection = getCollection("users")

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
                        val user = result.next() as CustomData
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
                val call: Call<ArrayList<Order_API>> = api.getOrder(userRole)
                val response = call.execute()
                if (response.isSuccessful) {
                    val orders = response.body()
                    if (orders != null) {

                        for (order in orders) {
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

    fun updateProducts(orderNumber: String) {
        localRealm.executeTransactionAsync {
            try {
                val api = RetrofitClient.getInstance().api
                val call: Call<ArrayList<Product_API>> = api.getProducts(orderNumber)
                val response = call.execute()
                if (response.isSuccessful) {
                    val products = response.body()
                    if (products != null) {
                        it.delete(Product_DB::class.java)
                        for (product in products)
                            it.copyToRealmOrUpdate(convertProduct(product))
                    }
                } else {
                    Log.e("Repository:  getProduct:   if", "Network Error")
                }
            } catch (error: IOException) {
                Log.e("Repository:  getProduct:  Catch", "Network Error")

            }

        }
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

    private fun  convertProduct (fromApi: Product_API): Product_DB {
        val item = Product_DB(fromApi.id, fromApi.name, fromApi.OrderNumber, fromApi.count)
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

    /** MongoDB methods **/

    fun getDeviations(id: String, onSuccess: (List<Deviation>) -> Unit, onError: (Exception) -> Unit) {

        val collection = getCollection("deviations") as MongoCollection<Deviation>

        val queryFilter = Document("owner_id", id)

        val deviations = mutableListOf<Deviation>()

        collection.find(queryFilter).iterator()
            .getAsync {
                if (it.isSuccess) {
                    val result = it.get()
                    while (result.hasNext()) {
                        val item = result.next() as Deviation
                        deviations.add(item)
                    }
                    onSuccess.invoke(deviations.toList())
                } else {
                    onError.invoke(it.error)
                }
            }
    }

    fun insertDeviation(
        deviation: Deviation,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ){
        val deviations = getCollection("deviations") as MongoCollection<Deviation>

        deviations.insertOne(deviation)?.getAsync {
            if (it.isSuccess) {
                Log.v(TAG(), "Deviation report with id ${deviation._id} inserted")
                onSuccess.invoke()
            } else {
                onError.invoke(it.error)
            }
        }
    }

    private fun getCollection(collectionName: String): MongoCollection<out Any?> {

        val type = when(collectionName) {
            "users" -> CustomData::class.java
            "deviations" -> Deviation::class.java
            "time_report" -> TimeReport::class.java
            else -> throw(Error("Collection type '$collectionName' not supported"))
        }

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
            collectionName,
            type
        ).withCodecRegistry(pojoCodecRegistry)

        Log.v(TAG(), "MongoDB collection collection handle instantiated")

        return collection
    }

}