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

        /**
         * Normal AppBar'da yer alan başlık, sola dayalı şekildedir.
         * Başlığın tasarımdaki gibi AppBar'ı ortalaması için, özel tasarım yapmak gerekir.
         * AppBar'ın görüntüleme seçeneğini "Özel Tasarım" olarak değiştir.
         */
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // Oluşturulan özel tasarımı AppBar'a ata.
        supportActionBar?.setCustomView(R.layout.activity_orders_appbar);

        setContentView(R.layout.activity_orders)

        /**
         * Sipariş Listesi, dikey ve her satırda bir öğe olacak şekilde tasarlanmıştır.
         * RecyclerView'de bu tasarımı içeren LayoutManager türü, Vertical LinearLayoutManager'dir.
         * LinearLayoutManager oluştur ve "orientation" değerini "Vertical (dikey)" yap.
         */
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Oluşturulan LayoutManager'i RecyclerView'e ata.
        ordersRcyc.layoutManager = linearLayoutManager

        // Swipe Refresh Layout'un ayarlamalarını yap.
        setSwipeRefresh()

        // Sipariş verilerini internetten çek.
        startCall()

    }

    fun setSwipeRefresh() {

        // Swipe Resfresh layout'un ikon rengini, primary color yap.
        ordersSwpLay.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))

        // Başlangıçta veriler yüklenene kadar, işlem yapıldığını gösteren ikonu görünür yap.
        ordersSwpLay.setRefreshing(true)

        // Ekran aşağı kaydırıldığında internetten verileri tekrar çek.
        ordersSwpLay.setOnRefreshListener {
            startCall()
        }
    }

    fun startCall() {

        // Retrofit arayüzünü çağır.
        val apiInterface = ApiClient.client?.create(ApiInterface::class.java)

        // Retrofit arayüzündeki tüm siparişleri getiren fonksiyonu çağrılmak üzere değişkene ata.
        val call = apiInterface?.getAllOrders()

        // Tüm siparişleri getiren çağrıyı çalıştır.
        call?.enqueue(object : Callback<List<Order>> {

            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {

                // Çağrı eğer başarıyla tamamlanırsa dönen verileri sipariş listesine dönüştür.
                val orders: List<Order>? = response.body();

                // Sipariş listesini içeren adaptörü oluştur.
                val adapter = OrderRcycAdapter(orders as ArrayList<Order>);

                // Adaptörü RecyclerView'e ata.
                ordersRcyc.adapter = adapter

                // İşlem tamamlandığı için, verilerin yüklenmekte olduğunu gösteren ikonu gizle.
                ordersSwpLay.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {

                /**
                 * İşlem tamamlandı ama bir hata nedeniyle veriler yüklenemedi.
                 * Hatayı konsola yazdır.
                 */
                Log.e("Orders / startCall ", t.message.toString());

                // İşlem hatayla da olsa tamamlandığı için, verilerin yüklenmekte olduğunu gösteren ikonu gizle.
                ordersSwpLay.isRefreshing = false
            }


        })
    }

    fun logout(view: View) {

        // Eğer kullanıcı "Çıkış Yap" butonuna tıklarsa, çıkmak isteyip istemediğini teyit eden bir uyarı göster.
        AlertDialog.Builder(this).setTitle(getString(R.string.act_orders_logout_dialog_title))
            .setMessage(R.string.act_orders_logout_dialog_content)
            .setPositiveButton(R.string.act_orders_logout_dialog_confirm) { dialog, id ->

                /**
                 * Eğer kullanıcı "İstiyorum" butonuna tıklarsa, öncelikle hafıdaki "remember" alanına "false" değerini ata.
                 * Böylece kullanıcı uygulamayı tekrar açtığında doğrudan "Siparişlerim" sayfasına yönlendirilmeyecektir.
                 * Çıkış yapıldığı için kullanıcının tekrar giriş yapması gerekecektir.
                 */
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putBoolean("remember", false).apply()

                // Kullanıcıyı "Giriş" sayfasına yönlendir.
                startActivity(Intent(this, LoginActivity::class.java))
            }.setNegativeButton(R.string.act_orders_logout_dialog_cancel) { dialog, id ->

                // Eğer kullanıcı "İptal" butonuna tıklarsa, uyarı ekranını kapat.
                dialog.cancel()
            }.show()

    }
}
