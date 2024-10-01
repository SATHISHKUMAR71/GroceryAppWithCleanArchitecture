package com.core.data.datasource.retailerdatasource

import com.core.domain.order.OrderDetails

interface OrderRetailerDataSource {
    fun getOrdersForRetailerWeeklySubscription():List<OrderDetails>
    fun getOrdersRetailerDailySubscription():List<OrderDetails>
    fun getOrdersForRetailerMonthlySubscription():List<OrderDetails>
    fun getOrdersForRetailerNoSubscription():List<OrderDetails>
    fun getAllOrders():List<OrderDetails>
}