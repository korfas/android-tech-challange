package com.korfas.marketim.model

import com.google.gson.annotations.SerializedName

abstract class Order {

    @SerializedName("productDetail")
    var productDetail: ProductDetail? = null

    @SerializedName("productState")
    var productState: String? = null

    @SerializedName("productPrice")
    var productPrice: Double = 0.toDouble()

    @SerializedName("orderName")
    var orderName: String? = null

    @SerializedName("marketName")
    var marketName: String? = null

    @SerializedName("month")
    var month: String? = null

    @SerializedName("date")
    var date: String? = null

    class ProductDetail {

        @SerializedName("summaryPrice")
        var summaryPrice: Double = 0.toDouble()

        @SerializedName("orderDetail")
        var orderDetail: String? = null
    }
}
