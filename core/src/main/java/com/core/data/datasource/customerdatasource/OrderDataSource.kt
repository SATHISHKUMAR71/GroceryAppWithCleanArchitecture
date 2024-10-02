package com.core.data.datasource.customerdatasource

import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData

interface OrderDataSource {
    fun getBoughtProductsList(userId: Int):List<OrderDetails>?
    fun getOrdersForUser(userID:Int):List<OrderDetails>?
    fun getOrdersForUserWeeklySubscription(userID:Int):List<OrderDetails>?
    fun getOrdersForUserDailySubscription(userID:Int):List<OrderDetails>?
    fun getOrdersForUserMonthlySubscription(userID:Int):List<OrderDetails>?
    fun getOrdersForUserNoSubscription(userID:Int):List<OrderDetails>?
    fun getOrder(cartId:Int):OrderDetails?
    fun addOrder(order:OrderDetails):Long?
    fun updateOrderDetails(orderDetails: OrderDetails)
    fun getOrderWithProductsWithOrderId(orderId: Int):Map<OrderDetails,List<CartWithProductData>>?
    fun getOrderDetailsWithOrderId(orderId:Int):OrderDetails?
}