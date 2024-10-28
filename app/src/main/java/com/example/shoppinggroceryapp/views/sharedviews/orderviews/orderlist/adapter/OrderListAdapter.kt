package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.db.entity.order.OrderDetailsEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.CartWithProductDataEntity
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.userviews.help.Help
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.diffutil.OrderListDiffUtil
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail.OrderDetailFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListViewModel

class OrderListAdapter(var orderedItems:MutableList<OrderDetails>, var fragment:Fragment, var clickable:Boolean?,var orderListViewModel: OrderListViewModel):RecyclerView.Adapter<OrderListAdapter.OrderLayoutViewHolder>() {

    var cartWithProductList = mutableListOf<MutableList<CartWithProductData>>()
    inner class OrderLayoutViewHolder(orderView:View):RecyclerView.ViewHolder(orderView){
        val productNames = orderView.findViewById<TextView>(R.id.orderedProductsList)
        val deliveryDate = orderView.findViewById<TextView>(R.id.deliveryDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderLayoutViewHolder {
        return OrderLayoutViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return orderedItems.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: OrderLayoutViewHolder, position: Int) {
        holder.deliveryDate.setTextColor(Color.BLACK)
        holder.deliveryDate.setBackgroundColor(Color.TRANSPARENT)

        if(orderedItems[position].deliveryFrequency=="Once") {
            holder.deliveryDate.setTextColor(Color.BLACK)
            holder.deliveryDate.visibility = View.VISIBLE
            when (orderedItems[position].deliveryStatus) {
                "Pending" -> {
                    val screen =
                        "Expected On: ${DateGenerator.getDayAndMonth(orderedItems[position].deliveryDate)}"
                    holder.deliveryDate.text = screen
                }

                "Cancelled" -> {
                    val screen = "Order Cancelled"
                    holder.deliveryDate.apply {
                        setTextColor(Color.WHITE)
                        setBackgroundColor(Color.RED)
                        text = screen
                    }
                }

                "Delivered" -> {
                    val screen =
                        "Delivered On: ${DateGenerator.getDayAndMonth(orderedItems[position].deliveryDate)}"
                    holder.deliveryDate.text = screen
                }

                else -> {
                    val screen =
                        "Expected On: ${DateGenerator.getDayAndMonth(orderedItems[position].deliveryDate)}"
                    holder.deliveryDate.text = screen
                }
            }

        }

        else{
            holder.deliveryDate.setTextColor(ContextCompat.getColor(fragment.requireContext(),R.color.offerColor))
//            holder.deliveryDate.setTextColor(androidx.appcompat.R.attr.colorControlHighlight)
            if(orderedItems[position].deliveryStatus!="Cancelled") {
                when (orderedItems[position].deliveryFrequency) {
                    "Weekly Once" -> {
                        orderListViewModel.getWeeklySubscriptionDateWithTimeSlot(orderedItems[position].orderId) {
                            var data = ""
                            for (i in it) {
                                data = orderListViewModel.getWeeklyPreparedData(
                                    i.key,
                                    i.value,
                                    orderedItems[position].orderedDate
                                )
                            }
                            MainActivity.handler.post {
                                holder.deliveryDate.text = data
                            }
                        }
                    }

                    "Monthly Once" -> {
                        orderListViewModel.getMonthlySubscriptionDateWithTime(orderedItems[position].orderId) {
                            var data = ""
                            for (i in it) {
                                data = orderListViewModel.getMonthlyPreparedDate(
                                    i.key,
                                    i.value,
                                    orderedItems[position].orderedDate
                                )
                            }
                            MainActivity.handler.post {
                                holder.deliveryDate.text = data
                            }
                        }
                    }

                    "Daily" -> {
                        orderListViewModel.getDailySubscriptionDateWithTime(orderedItems[position].orderId) {
                            var data = ""
                            for (i in it) {
                                data = orderListViewModel.getDailyPreparedData(
                                    i.key,
                                    i.value,
                                    orderedItems[position].orderedDate
                                )
                            }
                            MainActivity.handler.post {
                                holder.deliveryDate.text = data
                            }
                        }
                    }
                }
            }
            else{
                when (orderedItems[position].deliveryStatus) {
                    "Pending" -> {
                        val screen =
                            "Expected On: ${DateGenerator.getDayAndMonth(orderedItems[position].deliveryDate)}"
                        holder.deliveryDate.text = screen
                    }

                    "Cancelled" -> {
                        val screen = "Order Cancelled"
                        holder.deliveryDate.apply {
                            setTextColor(Color.WHITE)
                            setBackgroundColor(Color.RED)
                            text = screen
                        }
                    }

                    "Delivered" -> {
                        val screen =
                            "Delivered On: ${DateGenerator.getDayAndMonth(orderedItems[position].deliveryDate)}"
                        holder.deliveryDate.text = screen
                    }

                    else -> {
                        val screen =
                            "Expected On: ${DateGenerator.getDayAndMonth(orderedItems[position].deliveryDate)}"
                        holder.deliveryDate.text = screen
                    }
                }
            }

        }
        val date = "Ordered On: ${DateGenerator.getDayAndMonth(orderedItems[position].orderedDate)}"
        holder.itemView.findViewById<TextView>(R.id.orderedDate).text = date
        var productName=""
        var i =0
        for(cartWithProductData in cartWithProductList[position]){
            if(i==0){
                productName += cartWithProductData.productName+" (${cartWithProductData.totalItems}) ${cartWithProductData.productQuantity}"
                i=1
            }
            else{
                productName += " ,\n"+cartWithProductData.productName+" (${cartWithProductData.totalItems}) ${cartWithProductData.productQuantity}"
            }
        }
        holder.productNames.text = productName
        holder.itemView.setOnClickListener {
            if(clickable==true){
                Help.selectedOrder = orderedItems[position]
                FragmentTransaction.navigateWithBackstack(fragment.parentFragmentManager, Help(),"Help Fragment")
            }
            else {
                val orderDetailFragment = OrderDetailFragment()
                orderDetailFragment.arguments = Bundle().apply {
                    this.putInt("orderId",orderedItems[position].orderId)
                    this.putInt("cartId",orderedItems[position].cartId)
                    this.putInt("addressId",orderedItems[position].addressId)
                    this.putString("paymentMode",orderedItems[position].paymentMode)
                    this.putString("deliveryFrequency",orderedItems[position].deliveryFrequency)
                    this.putString("paymentStatus",orderedItems[position].paymentStatus)
                    this.putString("deliveryStatus",orderedItems[position].deliveryStatus)
                    this.putString("deliveryDate",orderedItems[position].deliveryDate)
                    this.putString("orderedDate",orderedItems[position].orderedDate)
                    for(i in cartWithProductList[position].indices){
                        putLong("productId$i",cartWithProductList[position][i].productId)
                        putString("mainImage$i",cartWithProductList[position][i].mainImage)
                        putString("productName$i",cartWithProductList[position][i].productName)
                        putString("productDescription$i",cartWithProductList[position][i].productDescription)
                        putInt("totalItems$i",cartWithProductList[position][i].totalItems)
                        putFloat("unitPrice$i",cartWithProductList[position][i].unitPrice)
                        putString("manufactureDate$i",cartWithProductList[position][i].manufactureDate)
                        putString("expiryDate$i",cartWithProductList[position][i].expiryDate)
                        putString("productQuantity$i",cartWithProductList[position][i].productQuantity)
                        putString("brandName$i",cartWithProductList[position][i].brandName)
                        putBoolean("isDeleted$i",orderListViewModel.isDeletedProduct[position][i])
                    }
                }
                fragment.parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                    .replace(R.id.fragmentMainLayout, orderDetailFragment)
                    .addToBackStack("Order Detail Fragment")
                    .commit()
            }
        }
    }

    fun setOrders(newList:MutableList<OrderDetails>){
        val orderDiffUtil = OrderListDiffUtil(orderedItems,newList)
        val orderDiffResults = DiffUtil.calculateDiff(orderDiffUtil)
        orderedItems.clear()
        orderedItems.addAll(newList)
        orderDiffResults.dispatchUpdatesTo(this)
    }
}