package com.example.soeco.Api

import com.example.soeco.Models.API_Models.Order_API
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    /*

    @GET("endpoint")   baseurl/endpoint
    fun function(): return promise with response type
 */
    @GET("orders")
    fun getOrder(): Call<ArrayList<Order_API>?>?





}
