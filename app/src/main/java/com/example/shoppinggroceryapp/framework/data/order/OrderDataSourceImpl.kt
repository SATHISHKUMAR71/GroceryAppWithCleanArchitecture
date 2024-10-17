package com.example.shoppinggroceryapp.framework.data.order

import com.core.data.datasource.orderdatasource.CustomerOrderDataSource
import com.core.data.datasource.orderdatasource.RetailerOrderDataSource
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData
import com.example.shoppinggroceryapp.framework.data.ConvertorHelper
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao

class OrderDataSourceImpl(private val retailerDao:RetailerDao):CustomerOrderDataSource,RetailerOrderDataSource {
    var convertorHelper = ConvertorHelper()
    override fun getBoughtProductsList(userId: Int): List<OrderDetails>? {
        return retailerDao.getBoughtProductsList(userId)?.let {
            convertorHelper.convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUser(userID: Int): List<OrderDetails>? {
        return retailerDao.getOrdersForUser(userID)?.let {
            convertorHelper.convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserWeeklySubscription(userID: Int): List<OrderDetails>? {
        return retailerDao.getOrdersForUserWeeklySubscription(userID)?.let {
            convertorHelper.convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserDailySubscription(userID: Int): List<OrderDetails>? {
        return retailerDao.getOrdersForUserDailySubscription(userID)?.let {
            convertorHelper.convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserMonthlySubscription(userID: Int): List<OrderDetails>? {
        return retailerDao.getOrdersForUserMonthlySubscription(userID)?.let {
            convertorHelper.convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserNoSubscription(userID: Int): List<OrderDetails>? {
        return retailerDao.getOrdersForUserNoSubscription(userID)?.let {
            convertorHelper.convertOrderDetailsEntityToOrderDetails(it)
        }
    }


    override fun getOrder(cartId: Int): OrderDetails? {

        return retailerDao.getOrder(cartId)?.let {
            convertorHelper.convertOrderEntityToOrderDetails(it)
        }
    }

    override fun addOrder(order: OrderDetails): Long? {
        return retailerDao.addOrder(convertorHelper.convertOrderDetailsToOrderDetailsEntity(order))
    }

    override fun updateOrderDetails(orderDetails: OrderDetails) {
        retailerDao.updateOrderDetails(convertorHelper.convertOrderDetailsToOrderDetailsEntity(orderDetails))
    }

    override fun getOrderWithProductsWithOrderId(orderId: Int): Map<OrderDetails, List<CartWithProductData>>? {
        val map:MutableMap<OrderDetails, List<CartWithProductData>> = mutableMapOf()

        retailerDao.getOrderWithProductsWithOrderId(orderId)?.let {orderDetailsMap ->
            for( i in orderDetailsMap){
                map[convertorHelper.convertOrderEntityToOrderDetails(i.key)] = i.value.map { CartWithProductData(it.productId,it.mainImage,it.productName,it.productDescription,it.totalItems,it.unitPrice,
                    it.manufactureDate,it.expiryDate,it.productQuantity,it.brandName) }
            }
        }
        return map
    }

    override fun getOrderDetailsWithOrderId(orderId: Int): OrderDetails? {
        return retailerDao.getOrderDetails(orderId)?.let {
            convertorHelper.convertOrderEntityToOrderDetails(it)
        }
    }

    override fun getOrderDetails(orderId: Int): OrderDetails? {
        return retailerDao.getOrderDetails(orderId)?.let {
            convertorHelper.convertOrderEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForRetailerWeeklySubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersForRetailerWeeklySubscription()?.let { convertorHelper.convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getOrdersRetailerDailySubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersRetailerDailySubscription()
            ?.let { convertorHelper.convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getOrdersForRetailerMonthlySubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersForRetailerMonthlySubscription()
            ?.let { convertorHelper.convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getOrdersForRetailerNoSubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersForRetailerNoSubscription()
            ?.let { convertorHelper.convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getAllOrders(): List<OrderDetails>? {
        return retailerDao.getOrderDetails()?.let { convertorHelper.convertOrderDetailsEntityToOrderDetails(it) }
    }

}