package com.example.soeco.Api

import android.app.Application
import android.util.Log
import com.example.soeco.Models.API_Models.Material_API
import com.example.soeco.Models.API_Models.Order_API
import com.example.soeco.Models.API_Models.Product_API
import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.Models.DB_Models.Product_DB
import io.realm.*
import retrofit2.Call
import java.io.IOException


class Repository (application: Application) {
    var materials: RealmResults<Material_DB>
    var orders: RealmResults<Order_DB>
    var products: RealmResults<Product_DB>

    private var realm : Realm

    init {

        val config = RealmConfiguration.Builder()
            .name("local-realm")
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .compactOnLaunch()
            .build()

        realm = Realm.getInstance(config)

        val productQuery =realm.where(Product_DB::class.java)
        val orderQuery =realm.where(Order_DB::class.java)
        val materialQuery =realm.where(Material_DB::class.java)
        materials = materialQuery.findAllAsync()
        products = productQuery.findAllAsync()
        orders = orderQuery.findAllAsync()

    }

    fun getOrder(id:String): Order_DB? {
        return realm.where(Order_DB::class.java).containsKey("OrderNumber", id).findFirst()

    }

    fun updateOrders(role: String) {
        realm.executeTransactionAsync {
            try {
                val api = RetrofitClient.getInstance().api
                val call: Call<ArrayList<Order_API>> = api.getOrder(role)
                val response = call.execute()
                if (response.isSuccessful) {
                    val orders = response.body()
                    if (orders != null) {
                        for (order in orders) {
                            for (product in order.Products)
                                it.copyToRealmOrUpdate(getProduct(product.id))

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


    private fun convertOrder(fromApi: Order_API): Order_DB {
        val product_List = RealmList<String>()
        for (product in fromApi.Products)
            product_List.add(product.toString())

        val contact_List = RealmList<String>()
        if(fromApi.contact!=null) {
            for (string in fromApi.contact)
                contact_List.add(string)
        }

        val item = Order_DB(
            fromApi.OrderNumber, product_List,
            fromApi.expectHours, fromApi.address, contact_List
        )
        return item
    }
    private fun  convertProduct (fromApi: Product_API): Product_DB {
        val item = Product_DB(fromApi.id, fromApi.name, fromApi.expectHours)
        return(item)
    }
    private fun getProduct(id: Int): Product_DB {
            try {
                val api = RetrofitClient.getInstance().api
                val call: Call<Product_API> = api.getProduct(id)
                val response = call.execute()
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        return(convertProduct(product))
                    }
                } else {
                    Log.e("Repository:  getProduct:   if", "Network Error")
                }
            } catch (error: IOException) {
                Log.e("Repository:  getProduct:  Catch", "Network Error")

        }
        return Product_DB(0,"error")
    }




   fun updateMaterials(role: String) {
       realm.executeTransactionAsync {
            try {
                val api = RetrofitClient.getInstance().api
                val call: Call<ArrayList<Material_API>> = api.getMaterial(role)
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


}