package com.example.shoppinggroceryapp.framework.data.order

import com.core.data.datasource.orderdatasource.CustomerOrderDataSource
import com.core.data.datasource.orderdatasource.RetailerOrderDataSource
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData
import com.example.shoppinggroceryapp.framework.data.ConvertorHelper
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao

class OrderDataSourceImpl(private val retailerDao:RetailerDao):CustomerOrderDataSource,RetailerOrderDataSource,ConvertorHelper() {
    override fun getBoughtProductsList(userId: Int): List<OrderDetails>? {
        return retailerDao.getBoughtProductsList(userId)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUser(userID: Int): List<OrderDetails>? {
        return retailerDao.getOrdersForUser(userID)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserWeeklySubscription(userID: Int): List<OrderDetails>? {
        return retailerDao.getOrdersForUserWeeklySubscription(userID)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserDailySubscription(userID: Int): List<OrderDetails>? {
        return retailerDao.getOrdersForUserDailySubscription(userID)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserMonthlySubscription(userID: Int): List<OrderDetails>? {
        return retailerDao.getOrdersForUserMonthlySubscription(userID)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserNoSubscription(userID: Int): List<OrderDetails>? {
        return retailerDao.getOrdersForUserNoSubscription(userID)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }


    override fun getOrder(cartId: Int): OrderDetails? {

        return retailerDao.getOrder(cartId)?.let {
            convertOrderEntityToOrderDetails(it)
        }
    }

    override fun addOrder(order: OrderDetails): Long? {
        return retailerDao.addOrder(convertOrderDetailsToOrderDetailsEntity(order))
    }

    override fun updateOrderDetails(orderDetails: OrderDetails) {
        retailerDao.updateOrderDetails(convertOrderDetailsToOrderDetailsEntity(orderDetails))
    }

    override fun getOrderWithProductsWithOrderId(orderId: Int): Map<OrderDetails, List<CartWithProductData>>? {
        val map:MutableMap<OrderDetails, List<CartWithProductData>> = mutableMapOf()

        retailerDao.getOrderWithProductsWithOrderId(orderId)?.let {orderDetailsMap ->
            for( i in orderDetailsMap){
                map[convertOrderEntityToOrderDetails(i.key)] = i.value.map { CartWithProductData(it.mainImage,it.productName,it.productDescription,it.totalItems,it.unitPrice,
                    it.manufactureDate,it.expiryDate,it.productQuantity,it.brandName) }
            }
        }
        return map
    }

    override fun getOrderDetailsWithOrderId(orderId: Int): OrderDetails? {
        return retailerDao.getOrderDetails(orderId)?.let {
            convertOrderEntityToOrderDetails(it)
        }
    }

    override fun getOrderDetails(orderId: Int): OrderDetails? {
        return retailerDao.getOrderDetails(orderId)?.let {
            convertOrderEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForRetailerWeeklySubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersForRetailerWeeklySubscription()?.let { convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getOrdersRetailerDailySubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersRetailerDailySubscription()
            ?.let { convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getOrdersForRetailerMonthlySubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersForRetailerMonthlySubscription()
            ?.let { convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getOrdersForRetailerNoSubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersForRetailerNoSubscription()
            ?.let { convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getAllOrders(): List<OrderDetails>? {
        return retailerDao.getOrderDetails()?.let { convertOrderDetailsEntityToOrderDetails(it) }
    }

}