package com.core.data.datasource.orderdatasource

import com.core.domain.order.OrderDetails

interface RetailerOrderDataSource {
    fun getOrdersForRetailerWeeklySubscription():List<OrderDetails>?
    fun getOrdersRetailerDailySubscription():List<OrderDetails>?
    fun getOrdersForRetailerMonthlySubscription():List<OrderDetails>?
    fun getOrdersForRetailerNoSubscription():List<OrderDetails>?
    fun getAllOrders():List<OrderDetails>?
}