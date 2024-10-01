package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData
import com.core.usecases.customerusecase.cart.GetDeletedProductsWithCarId
import com.core.usecases.customerusecase.cart.GetProductsWithCartData
import com.core.usecases.customerusecase.orders.GetOrderForUser
import com.core.usecases.customerusecase.orders.GetOrderForUserDailySubscription
import com.core.usecases.customerusecase.orders.GetOrderForUserMonthlySubscription
import com.core.usecases.customerusecase.orders.GetOrderForUserWeeklySubscription
import com.core.usecases.customerusecase.orders.GetOrdersForUserNoSubscription
import com.core.usecases.retailerusecase.orders.GetAllOrders
import com.core.usecases.retailerusecase.orders.GetDailyOrders
import com.core.usecases.retailerusecase.orders.GetMonthlyOrders
import com.core.usecases.retailerusecase.orders.GetNormalOrder
import com.core.usecases.retailerusecase.orders.GetWeeklyOrders
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.entity.order.OrderDetailsEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.CartWithProductDataEntity

class OrderListViewModel(private var mGetOrderForUser: GetOrderForUser,
                         private var mGetOrderForUserMonthlySubscription: GetOrderForUserMonthlySubscription,
                         private var mGetOrderForUserDailySubscription: GetOrderForUserDailySubscription,
                         private var mGetOrderForUserWeeklySubscription: GetOrderForUserWeeklySubscription,
                         private var mGetOrdersForUserNoSubscription: GetOrdersForUserNoSubscription,
                         private val mGetProductsWithCartData: GetProductsWithCartData,
                         private val mGetDeletedProductsWithCarId: GetDeletedProductsWithCarId,
                         private val mGetWeeklyOrders: GetWeeklyOrders,
                         private val mGetMonthlyOrders: GetMonthlyOrders,
                         private val mGetNormalOrder: GetNormalOrder,
                         private val mGetDailyOrders: GetDailyOrders,
                         private val mGetAllOrders: GetAllOrders):ViewModel() {

    var orderedItems:MutableLiveData<List<OrderDetails>> = MutableLiveData()
    var dataReady:MutableLiveData<Boolean> = MutableLiveData()
    private var lock =Any()
    var cartWithProductList:MutableLiveData<MutableList<MutableList<CartWithProductData>>> =
        MutableLiveData<MutableList<MutableList<CartWithProductData>>>().apply {
            value = mutableListOf()
        }

    fun getOrdersForSelectedUser(userId:Int){
        Thread {

            orderedItems.postValue(mGetOrderForUser.invoke(userId))
        }.start()
    }

    fun getOrdersForSelectedUserWithNoSubscription(userId:Int){
        Thread {
            orderedItems.postValue(mGetOrdersForUserNoSubscription.invoke(userId))
        }.start()
    }
    fun getOrdersForRetailerWithNoSubscription(userId:Int){
        Thread {
            orderedItems.postValue(mGetNormalOrder.invoke())
        }.start()
    }

    fun getOrdersForSelectedUserDailySubscription(userId:Int){
        Thread {

            orderedItems.postValue(mGetOrderForUserDailySubscription.invoke(userId))
        }.start()
    }

    fun getOrdersForRetailerDailySubscription(userId:Int){
        Thread {
            orderedItems.postValue(mGetDailyOrders.invoke())
        }.start()
    }

    fun getOrdersForSelectedUserWeeklySubscription(userId:Int){
        Thread {

            orderedItems.postValue(mGetOrderForUserWeeklySubscription.invoke(userId))
        }.start()
    }

    fun getOrdersForRetailerWeeklySubscription(userId:Int){
        Thread {

            orderedItems.postValue(mGetWeeklyOrders.invoke())
        }.start()
    }

    fun getOrdersForSelectedUserMonthlySubscription(userId:Int){
        Thread {
            orderedItems.postValue(mGetOrderForUserMonthlySubscription.invoke(userId))
        }.start()
    }
    fun getOrdersForRetailerMonthlySubscription(userId:Int){
        Thread {

            orderedItems.postValue(mGetMonthlyOrders.invoke())
        }.start()
    }

    fun getOrderedItemsForRetailer(){
        Thread{
            orderedItems.postValue(mGetAllOrders.invoke())
        }.start()
    }

    fun getCartWithProducts(){

        Thread {
            for(i in orderedItems.value!!) {
                synchronized(lock) {
                    val tmpList = mGetProductsWithCartData.invoke(i.cartId).toMutableList()
                    tmpList.addAll(mGetDeletedProductsWithCarId.invoke(i.cartId))
                    cartWithProductList.value!!.add(
                        tmpList
                    )

                    for(j in tmpList){
                    }
                }
            }
            dataReady.postValue(true)
        }.start()
    }
}