package com.korfas.marketim.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
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

    fun logout(view: View) {

        AlertDialog.Builder(this).setTitle(getString(R.string.act_orders_logout_dialog_title))
            .setMessage(R.string.act_orders_logout_dialog_content)
            .setPositiveButton(R.string.act_orders_logout_dialog_confirm) { dialog, id ->
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("remember", false).apply()
                startActivity(Intent(this, LoginActivity::class.java))
            }.setNegativeButton(R.string.act_orders_logout_dialog_cancel) { dialog, id ->
                dialog.cancel()
            }.show()

    }
}
