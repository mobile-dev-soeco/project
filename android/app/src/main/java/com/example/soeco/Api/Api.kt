package com.example.soeco.Api

import com.example.soeco.Models.API_Models.Order_API
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    /*

    @GET("endpoint")   baseurl/endpoint
    fun function(): return promise with response type
 */
    @GET("orders")
    fun getOrder(@Query("role") role :String) : Call<ArrayList<Order_API>>


}
