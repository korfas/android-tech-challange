package com.korfas.marketim.retrofit

import com.korfas.marketim.model.Order
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("/")
    fun getAllOrders(): Call<List<Order>>
}