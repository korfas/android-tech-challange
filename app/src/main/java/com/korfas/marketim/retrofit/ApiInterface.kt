package com.korfas.marketim.retrofit

import retrofit2.http.GET

interface ApiInterface {

    @GET()
    fun getAllOrders(){

    }
}