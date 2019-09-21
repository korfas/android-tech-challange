package com.korfas.marketim.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // Verilerin çekileceği URL
    val BASE_URL = "http://kariyertechchallenge.mockable.io"

    /**
     * Singleton Pattern'i kullanılmıştır.
     * Oluşturulan Retrofit objesine öncelikle "null" değeri atanmıştır.
     * Ardından "client" çağrıldığında eğer retrofit nesnesi "null" ise retrofit nesnesi Builder Pattern'i ile oluşturulmuştur.
     * Eğer retrofit nesnesi "null" değil ise mevcut nesne döndürülmüştür.
     */

    private var retrofit: Retrofit? = null

    val client: Retrofit?
        get() {
            if (retrofit == null) {


                // JSON'dan POJO'ya dönüşüm yapılması için "GSON" kullanılmıştır.
                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit
        }
}