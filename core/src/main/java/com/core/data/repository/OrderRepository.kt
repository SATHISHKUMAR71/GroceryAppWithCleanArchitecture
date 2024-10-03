package com.core.data.repository

import com.core.data.datasource.orderdatasource.CustomerOrderDataSource
import com.core.data.datasource.orderdatasource.RetailerOrderDataSource
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData

class OrderRepository(private val customerOrderDataSource: CustomerOrderDataSource,private val retailerOrderDataSource: RetailerOrderDataSource) {

    fun getOrdersForRetailerWeeklySubscription(): List<OrderDetails>? {
        return retailerOrderDataSource.getOrdersForRetailerWeeklySubscription()
    }

    fun getOrdersRetailerDailySubscription(): List<OrderDetails>? {
        return retailerOrderDataSource.getOrdersRetailerDailySubscription()
    }

    fun getOrdersForRetailerMonthlySubscription(): List<OrderDetails>? {
        return retailerOrderDataSource.getOrdersForRetailerMonthlySubscription()
    }

    fun getOrdersForRetailerNoSubscription(): List<OrderDetails>? {
        return retailerOrderDataSource.getOrdersForRetailerNoSubscription()
    }

    fun getAllOrders():List<OrderDetails>?{
        return retailerOrderDataSource.getAllOrders()
    }

    fun getBoughtProductsList(userId: Int):List<OrderDetails>?{
        return customerOrderDataSource.getBoughtProductsList(userId)
    }
    fun getOrdersForUser(userID:Int):List<OrderDetails>? {
        return customerOrderDataSource.getOrdersForUser(userID)
    }
    fun getOrdersForUserWeeklySubscription(userID:Int):List<OrderDetails>? {
        return customerOrderDataSource.getOrdersForUserWeeklySubscription(userID)
    }
    fun getOrdersForUserDailySubscription(userID:Int):List<OrderDetails>? {
        return customerOrderDataSource.getOrdersForUserDailySubscription(userID)
    }

    fun getOrdersForUserMonthlySubscription(userID:Int):List<OrderDetails>? {
        return customerOrderDataSource.getOrdersForUserMonthlySubscription(userID)
    }

    fun getOrdersForUserNoSubscription(userID:Int):List<OrderDetails>? {
        return customerOrderDataSource.getOrdersForUserNoSubscription(userID)
    }

    fun getOrder(cartId:Int):OrderDetails?{
        return customerOrderDataSource.getOrder(cartId)
    }
    fun addOrder(order: OrderDetails): Long? {
        return customerOrderDataSource.addOrder(order)
    }

    fun updateOrderDetails(orderDetails: OrderDetails){
        customerOrderDataSource.updateOrderDetails(orderDetails)
    }

    fun getOrderWithProductsWithOrderId(orderId: Int):Map<OrderDetails,List<CartWithProductData>>?{
        return customerOrderDataSource.getOrderWithProductsWithOrderId(orderId)
    }

    fun getOrderDetails(orderId:Int):OrderDetails?{
        return customerOrderDataSource.getOrderDetails(orderId)
    }

}