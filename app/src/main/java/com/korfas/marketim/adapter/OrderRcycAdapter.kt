package com.korfas.marketim.adapter

import android.content.Context
import android.graphics.drawable.Animatable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.korfas.marketim.R
import com.korfas.marketim.model.Order
import kotlinx.android.synthetic.main.activity_orders_rcyc_model.view.*
import java.util.*
import kotlin.collections.ArrayList
import android.view.animation.Animation
import android.view.animation.ScaleAnimation


class OrderRcycAdapter(var orders: ArrayList<Order>) :
    RecyclerView.Adapter<OrderRcycAdapter.OrderViewHolder>() {

    /**
     * Constructor içerisinde verilen "orders" değişkeni, siparişlerden oluşan bir liste tutar.
     * Bu liste içindeki siparişler, RecyclerView içerisinde belli bir görsel tasarıma göre gösterilecektir.
     * Listedeki her öğe, bu tasarıma uygun olarak, alt alta listelencektir.
     * Bu tasarım xml formatında yapılmıştır. Tasarıma ait öğelerin bu dosyada kullanılması gerekir.
     * Tasarım öğelerini kullanmak için gerekli olan sınıf, "ViewHolder" sınıfıdır.
     * Oluşturulan ViewHolder, tasarım içerindeki View öğelerine erişim sağlar
     */

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var orderView = itemView as ConstraintLayout

        var context = itemView.context
        var dateTv = itemView.dateTv
        var monthTv = itemView.monthTv
        var marketNameTv = itemView.marketNameTv
        var orderNameTv = itemView.orderNameTv
        var productStateCard = itemView.productStateCard
        var productStateTv = itemView.productStateTv
        var expansionArrowImgv = itemView.expansionArrowImgv
        var productPriceTv = itemView.productPriceTv

        var productDetailBackgroundView = itemView.productDetailBackgroundView
        var productDetailConstLay = itemView.productDetailConstLay
        var orderDetailTv = itemView.orderDetailTv
        var summaryPriceTv = itemView.summaryPriceTv
    }

    /**
     * ViewHolder içerisinde erişilen öğelerin, hangi xml dosyası içerisinden çekileceği belirtilmelidir.
     * Xml dosyasının Java veya Kotlin sınıfları aracılığıyla erişilebilir hale getirilmesi işlemine "İnflating" denir.
     * RecyclerView adaptörü içerisinde "inflating" işleminin yapıldığı metod, "onCreateViewHolder" metodudur.
     * Bu metod içerisinde xml dosyası "inflate" edilir ve ViewHolder'a bağlanır.
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val modelView = inflater.inflate(R.layout.activity_orders_rcyc_model, parent, false)
        return OrderViewHolder(modelView)
    }

    /**
     * RecyclerView içerinde kaç adet öğe olacağı, "getItemCount" metodu içerisinde belirtilmelidir.
     * Bu örnekte sabit sayıda değil, constructor içerisinde verilen listede kaç adet eleman varsa o kadar satır oluşturulacaktır.
     * Bu yüzden satır sayısı, listenin eleman sayısıdır.
     */

    override fun getItemCount(): Int {
        return orders.size
    }

    /**
     * "inflate" işlemi tamamlanıp ViewHolder oluşturulduktan sonra "onBindViewHolder" çalışır.
     * Bu metod, listedeki her eleman için tek tek çalışır.
     * Her satır için hangi View'e hangi değer atanacağı burada belirlenir.
     */

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        /**
         * Metod, her seferinde "position" değerini 1 arttırarak, listenin her elemanı için çalışacaktır.
         * Listenin mevcut "position" değerindeki elemanını al ve bir değişkene ata.
         */
        val order = orders.get(position)


        /**
         * Siparişin üç adet olası durumu olabilir ve her bir durum için bir renk belirlenmiştir.
         * Siparişin durumunu kontrol et ve durum rengini bir değişkene ata.
         */
        val stateColor = when (order.productState?.trim()?.toLowerCase(Locale.getDefault())) {

            holder.context.getString(R.string.product_state_preparing).trim().toLowerCase(Locale.getDefault()) -> ContextCompat.getColor(
                holder.context,
                R.color.colorProductStatePreparing
            )
            holder.context.getString(R.string.product_state_confirmation).trim().toLowerCase(Locale.getDefault()) -> ContextCompat.getColor(
                holder.context,
                R.color.colorProductStateConfirmation
            )
            holder.context.getString(R.string.product_state_delivering).trim().toLowerCase(Locale.getDefault()) -> ContextCompat.getColor(
                holder.context,
                R.color.colorProductStateDelivering
            )
            else -> ContextCompat.getColor(holder.context, R.color.colorProductStatePreparing)

        }

        // Sipariş durumuna ait rengi, tasarımdaki durum kartına ve durum yazısına ata.
        holder.productStateCard.setCardBackgroundColor(stateColor)
        holder.productStateTv.setTextColor(stateColor)

        /**
         * Siparişe ait tüm verileri (date, month, marketName vb.) sırasıyla ViewHolder içerisindeki görsele yerleştir.
         */
        holder.dateTv.text = order.date
        holder.monthTv.text =
            holder.context.resources.getStringArray(R.array.months)[Integer.valueOf(order.month?.toInt()!! - 1)]
        holder.marketNameTv.text = order.marketName
        holder.orderNameTv.text = order.orderName
        holder.productStateTv.text = order.productState
        holder.productPriceTv.text =
            holder.context.getString(R.string.act_orders_product_price, order.productPrice)

        holder.orderDetailTv.text = order.productDetail?.orderDetail
        holder.summaryPriceTv.text = holder.context.getString(
            R.string.act_orders_product_price,
            order.productDetail?.summaryPrice
        )


        /**
         * Sipariş detayının açık olup olmadığı bilgisini bir değişkene ata.
         * Başlangıçta kapalı olacağı için değişkenin ilk değerini "false" yap.
         */
        var expanded = false

        holder.itemView.setOnClickListener {

            /**
             * Öğeye tıklandığında, ok animasyonu çalışacaktır.
             * Eğer detay kapalıysa, ImageView'e sağa dönük ok atanacak ve aşağı doğru animasyonu sağlanacaktır.
             * Eğer detay açıksa, ImageView'e aşağı dönük ok atanacak ve sağa doğru animasyonu sağlanacaktır.
             */

            // Duruma göre sağa veya aşağı dönük ok resmini seç.
            val drawableResource =
                if (expanded) R.drawable.ic_expansion_arrow_bottom else R.drawable.ic_expansion_arrow_right

            // Seçilen resmi ImageView'a ata.
            holder.expansionArrowImgv.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.context,
                    drawableResource
                )
            )

            // ImageView içerindeki görseli, animasyonu çalıştırmak için "Animatable" türünde çağır.
            val animatable = holder.expansionArrowImgv.drawable as Animatable

            // Animasyonu çalıştır.
            animatable.start()

            /**
             * Eğer detay önceden açıksa, tıklandıktan sonra kapalı olarak değiştir.
             * Eğer detay önceden kapalıysa, tıklandıktan sonra açık olarak değiştir.
             */
            expanded = !expanded;

            // Detay öğesinin görünürlüğünü animasyon ile değiştir.
            startExpandAnimation(holder.context, holder.productDetailBackgroundView, expanded)
            startExpandAnimation(holder.context, holder.productDetailConstLay, expanded)

        }
    }



    fun startExpandAnimation(context: Context, view: View, expanded: Boolean) {

        /**
         * Eğer detay açılacaksa, detayın boyutu 0 katından 1 katına çıkacak şekilde animasyon değişkeni oluştur.
         * Eğer detay kapanacaksa, detayın boyutu 1 katından 0 katına düşecek şekilde animasyon değişkeni oluştur.
         */
        val anim = if (expanded) ScaleAnimation(1f, 1f, 0f, 1f) else ScaleAnimation(1f, 1f, 1f, 0f);

        /**
         * Animasyon süresini seç.
         * Buradaki süre değeri "integers.xml" dosyasına aktarılmıştır.
         * Bunun sebebi, bu animasyon ve okun sağdan aşağıya, aşağıdan sağa olan animasyonları ile birlikte üç adet animasyon olmasıdır.
         * Bu üç animasyonda aynı zamanda bitmeli, aynı süreyi almalıdır.
         * Bunun için hepsine "integers.xml" içerisindeki "animation_duration" değeri atanmıştır.
         * Bu sayede animasyon süresini değiştirmek için bu değeri değiştirmek yeterli olacaktır.
         */
        anim.duration = context.resources.getInteger(R.integer.animation_duration).toLong()

        // Animasyon bitince değişimin son değerini görsele uygula
        anim.fillAfter = true

        // Animasyonu başlat
        view.startAnimation(anim)

        anim.setAnimationListener(object : Animation.AnimationListener {


            // Animasyon başladığında, eğer detay açılacak ise önce görünürlüğü aç, ardından animasyonu sürdür.
            override fun onAnimationStart(animation: Animation) {
                if (expanded) view.visibility = View.VISIBLE
            }

            // Eğer detay kapanacaksa, animasyon bittiğinde görünürlüğü kapat.
            override fun onAnimationEnd(animation: Animation) {
                if (!expanded) view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }


}