package com.korfas.marketim.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.korfas.marketim.R
import com.korfas.marketim.adapter.OrderRcycAdapter
import com.korfas.marketim.model.Order
import com.korfas.marketim.retrofit.ApiClient
import com.korfas.marketim.retrofit.ApiInterface
import kotlinx.android.synthetic.main.activity_orders.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        supportActionBar?.setCustomView(R.layout.activity_orders_appbar);
        setContentView(R.layout.activity_orders)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ordersRcyc.layoutManager = linearLayoutManager

        setSwipeRefresh()
        startCall()

    }

    fun setSwipeRefresh() {

        ordersSwpLay.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        ordersSwpLay.setRefreshing(true)
        ordersSwpLay.setOnRefreshListener {
            startCall()
        }
    }

    fun startCall() {
        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        val call = apiInterface?.getAllOrders()

        call?.enqueue(object : Callback<List<Order>> {

            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {

                val orders: List<Order>? = response.body();
                val adapter = OrderRcycAdapter(orders as ArrayList<Order>);
                ordersRcyc.adapter = adapter
                ordersSwpLay.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                ordersSwpLay.isRefreshing = false
            }


        })
    }
}
