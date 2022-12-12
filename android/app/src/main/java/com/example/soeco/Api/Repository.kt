package com.example.soeco.Api

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.soeco.Models.API_Models.Order_API
import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.Models.DB_Models.Product_DB
import com.example.soeco.RoomDb.DAO
import com.example.soeco.RoomDb.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import java.io.IOException
import java.util.ArrayList

class Repository (application: Application){
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    var materials: LiveData<List<Material_DB>>
    var orders: LiveData<List<Order_DB>>
    var products: LiveData<List<Product_DB>>

    private var dao: DAO?
    init {

        val db: RoomDB? =
            RoomDB.getDatabase(application)
        dao = db?.dao()
        materials = dao?.getMaterials()!!
        orders = dao?.getOrders()!!
        products = dao?.getProducts()!!

    }


    fun updateOrders() {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val api = RetrofitClient.getInstance().api
                val call: Call<ArrayList<Order_API>?>? = api.getOrder()
                val response = call?.execute()
                if (response?.isSuccessful == true){
                    val orders = response.body()
                    dao?.clearOrders() // deletes all orders when orders are fetched should probably do it smarter
                    if (orders != null) {
                        for (item in orders){
                            insertOrder(item)
                        }
                    }
                }
                else{
                    Log.e("Repository:  Update:   if","Network Error")

                }
            }
            catch (error : IOException){
                Log.e("Repository:  Update:  Catch","Network Error")
            }
        }
    }

    private fun insertOrder(fromApi: Order_API) {
        val item = Order_DB(fromApi.title)
        dao?.insertOrder(item)
    }

}