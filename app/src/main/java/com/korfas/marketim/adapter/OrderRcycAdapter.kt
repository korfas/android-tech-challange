package com.korfas.marketim.adapter

import android.graphics.drawable.Animatable
import android.util.Log
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
import android.opengl.ETC1.getWidth
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.animation.ScaleAnimation


class OrderRcycAdapter(var orders: ArrayList<Order>) :
    RecyclerView.Adapter<OrderRcycAdapter.OrderViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val modelView = inflater.inflate(R.layout.activity_orders_rcyc_model, parent, false)
        return OrderViewHolder(modelView)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val order = orders.get(position)

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

        holder.productStateCard.setCardBackgroundColor(stateColor)
        holder.productStateTv.setTextColor(stateColor)

        var expanded = false

        holder.itemView.setOnClickListener {

            val drawableResource =
                if (expanded) R.drawable.ic_expansion_arrow_bottom else R.drawable.ic_expansion_arrow_right

            holder.expansionArrowImgv.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.context,
                    drawableResource
                )
            )
            val animatable = holder.expansionArrowImgv.drawable as Animatable
            animatable.start()

            expanded = !expanded;

            startExpandAnimation(holder.productDetailBackgroundView, expanded)
            startExpandAnimation(holder.productDetailConstLay, expanded)

        }
    }

    fun startExpandAnimation(view: View, expanded: Boolean) {
        val anim =
            if (expanded) ScaleAnimation(1f, 1f, 0f, 1f) else ScaleAnimation(1f, 1f, 1f, 0f);
        anim.duration = 200
        anim.fillAfter = true
        view.startAnimation(anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (expanded) view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                if (!expanded) view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }


}