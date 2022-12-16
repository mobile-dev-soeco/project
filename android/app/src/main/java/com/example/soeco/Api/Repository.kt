package com.example.soeco.Api

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.soeco.Models.API_Models.Material_API
import com.example.soeco.Models.API_Models.Order_API
import com.example.soeco.Models.API_Models.Product_API
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
import kotlin.collections.ArrayList

class Repository (application: Application) {
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


    fun updateOrders(role: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val api = RetrofitClient.getInstance().api
                val call: Call<ArrayList<Order_API>> = api.getOrder(role)
                val response = call.execute()
                if (response.isSuccessful) {
                    val orders = response.body()
                    dao?.clearOrders() // deletes all orders when orders are fetched should probably do it smarter
                    if (orders != null) {
                        for (order in orders) {
                            for (product in order.Products)
                                getProduct(product.id)
                            insertOrder(order)
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


    private fun insertOrder(fromApi: Order_API) {
        val item = Order_DB(
            fromApi.OrderNumber, fromApi.Products,
            fromApi.expectHours, fromApi.address, fromApi.contact
        )
        dao?.insertOrder(item)
    }

    fun getProduct(id: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val api = RetrofitClient.getInstance().api
                val call: Call<Product_API> = api.getProduct(id)
                val response = call.execute()
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        insertProducts(product)
                    }
                } else {
                    Log.e("Repository:  getProduct:   if", "Network Error")
                }
            } catch (error: IOException) {
                Log.e("Repository:  getProduct:  Catch", "Network Error")
            }
        }
    }

    private fun insertProducts(fromApi: Product_API) {
        val item = Product_DB(fromApi.id, fromApi.name, fromApi.expectHours)
        dao?.insertProduct(item)
    }

    fun updateMaterials(role: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val api = RetrofitClient.getInstance().api
                val call: Call<ArrayList<Material_API>> = api.getMaterial(role)
                val response = call.execute()
                if (response.isSuccessful) {
                    val materials = response.body()
                    if (materials != null) {
                        for (material in materials) {
                            insertMaterial(material)
                        }
                    }
                }
            } catch (error: IOException) {
                Log.e("Repository:  getProduct:  Catch", "Network Error")
            }
        }
    }

    private fun insertMaterial(fromApi: Material_API) {
        val item = Material_DB(fromApi.id, fromApi.name, fromApi.unit)
        dao?.insertMaterial(item)
    }


}