package com.korfas.marketim.retrofit

import com.korfas.marketim.model.Order
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    /**
     * Veriler doğrudan BASE_URL'den çekilecektir.
     * Tek bir veri çekme fonksiyonu olduğu için tek bir URL kullanılmıştır.
     * Farklı URL uzantıları kullanılmamıştır.
     */
    @GET("/")
    fun getAllOrders(): Call<List<Order>>
}