package com.example.soeco.data.Api

import com.example.soeco.data.Models.API_Models.Contact_API
import com.example.soeco.data.Models.API_Models.Material_API
import com.example.soeco.data.Models.API_Models.Order_API
import com.example.soeco.data.Models.API_Models.Product_API
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    /*

    @GET("endpoint")   baseurl/endpoint
    fun function(): return promise with response type
 */
    @GET("{fullUrl}")
    fun getOrder(@Header("Authorization") authkey:String, @Path(value = "fullUrl", encoded = true) fullUrl: String): Call<ArrayList<Order_API>>

    @GET("{fullUrl}")
    fun getMaterial(@Header("Authorization") authkey:String, @Path(value = "fullUrl", encoded = true) fullUrl: String): Call<ArrayList<Material_API>>

    @GET("{fullUrl}")
    fun getContact(@Header("Authorization") authkey:String, @Path(value = "fullUrl", encoded = true) fullUrl: String): Call<ArrayList<Contact_API>>

    @GET("{fullUrl}")
    fun getProducts(@Header("Authorization") authkey:String, @Path(value = "fullUrl", encoded = true) fullUrl: String): Call<ArrayList<Product_API>>
}

