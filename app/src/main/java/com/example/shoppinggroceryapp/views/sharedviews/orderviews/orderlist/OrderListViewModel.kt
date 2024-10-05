package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData
import com.core.usecases.cartusecase.getcartusecase.GetDeletedProductsWithCarId
import com.core.usecases.cartusecase.getcartusecase.GetProductsWithCartData
import com.core.usecases.orderusecase.getordersusecase.GetOrderForUser
import com.core.usecases.subscriptionusecase.getsubscriptionusecase.GetOrderForUserDailySubscription
import com.core.usecases.subscriptionusecase.getsubscriptionusecase.GetOrderForUserMonthlySubscription
import com.core.usecases.subscriptionusecase.getsubscriptionusecase.GetOrderForUserWeeklySubscription
import com.core.usecases.subscriptionusecase.getsubscriptionusecase.GetOrdersForUserNoSubscription
import com.core.usecases.orderusecase.getordersusecase.GetAllOrders
import com.core.usecases.orderusecase.getordersusecase.GetDailyOrders
import com.core.usecases.orderusecase.getordersusecase.GetMonthlyOrders
import com.core.usecases.orderusecase.getordersusecase.GetNormalOrder
import com.core.usecases.orderusecase.getordersusecase.GetWeeklyOrders
import com.example.shoppinggroceryapp.MainActivity

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
                         private val mGetAllOrders: GetAllOrders
):ViewModel() {

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


    fun getOrdersBasedOnSubscription(orderItems:List<OrderDetails>,subscriptionType:String?,isRetailer:Boolean):String{
        if(orderItems.isEmpty()){
            when (subscriptionType) {
                "Weekly Once" -> {
                    if(isRetailer){
                        getOrdersForRetailerWeeklySubscription(MainActivity.userId.toInt())
                    }
                    else {
                        getOrdersForSelectedUserWeeklySubscription(MainActivity.userId.toInt())
                    }
                    return "Weekly Orders"
                }

                "Monthly Once" -> {
                    if(isRetailer){
                        getOrdersForRetailerMonthlySubscription(MainActivity.userId.toInt())
                    }
                    else {
                        getOrdersForSelectedUserMonthlySubscription(MainActivity.userId.toInt())
                    }
                    return "Monthly Orders"
                }

                "Daily" -> {
                    if(isRetailer){
                        getOrdersForRetailerDailySubscription(MainActivity.userId.toInt())
                    }
                    else {
                        getOrdersForSelectedUserDailySubscription(MainActivity.userId.toInt())
                    }
                    return "Daily Orders"
                }

                "Once" -> {
                    if(isRetailer){
                        getOrdersForRetailerWithNoSubscription(MainActivity.userId.toInt())
                    }
                    else {
                        getOrdersForSelectedUserWithNoSubscription(MainActivity.userId.toInt())
                    }
                    return "One Time Orders"
                }

                else ->{
                    getOrdersForSelectedUser(MainActivity.userId.toInt())
                }
            }
        }
        return "My Orders"
    }
}