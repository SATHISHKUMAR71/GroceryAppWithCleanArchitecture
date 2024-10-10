package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.OrderDetails
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
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
import com.core.usecases.orderusecase.getordersusecase.GetDailySubscriptionOrderWithTimeSlot
import com.core.usecases.orderusecase.getordersusecase.GetMonthlyOrders
import com.core.usecases.orderusecase.getordersusecase.GetMonthlySubscriptionWithTimeSlot
import com.core.usecases.orderusecase.getordersusecase.GetNormalOrder
import com.core.usecases.orderusecase.getordersusecase.GetOrderedTimeSlot
import com.core.usecases.orderusecase.getordersusecase.GetSpecificWeeklyOrderWithTimeSlot
import com.core.usecases.orderusecase.getordersusecase.GetWeeklyOrders
import com.core.usecases.orderusecase.updateorderusecase.UpdateOrderDetails
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator

class OrderListViewModel(private var mGetOrderForUser: GetOrderForUser,
                         private var mUpdateOrderDetails: UpdateOrderDetails,
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
                         private val mGetAllOrders: GetAllOrders,
                         private val mGetDailyOrderWithTimeSlot:GetDailySubscriptionOrderWithTimeSlot,
                         private val mGetSpecificMonthlyOrderWithTimeSlot: GetMonthlySubscriptionWithTimeSlot,
                         private val mGetSpecificWeeklyOrderWithTimeSlot: GetSpecificWeeklyOrderWithTimeSlot,
                         private val mGetOrderedTimeSlot: GetOrderedTimeSlot):ViewModel() {

    var orderedItems:MutableLiveData<List<OrderDetails>> = MutableLiveData()
    var days = listOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
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

    fun getDailySubscriptionDateWithTime(orderId:Int,callback: (Map<DailySubscription,TimeSlot>) -> Unit){
        Thread{
            mGetDailyOrderWithTimeSlot.invoke(orderId)?.let {
                callback(it)
            }
        }.start()
    }

    fun getMonthlySubscriptionDateWithTime(orderId:Int,callback: (Map<MonthlyOnce,TimeSlot>) -> Unit){
        Thread{
            mGetSpecificMonthlyOrderWithTimeSlot.invoke(orderId)?.let {
                callback(it)
            }
        }.start()
    }

    fun getWeeklySubscriptionDateWithTimeSlot(orderId:Int,callback:(Map<WeeklyOnce,TimeSlot>) -> Unit){
        Thread{
            mGetSpecificWeeklyOrderWithTimeSlot.invoke(orderId)?.let {
                callback(it)
            }
        }.start()
    }

    fun getTimeSlot(orderId: Int){
        Thread{
            mGetOrderedTimeSlot.invoke(orderId)?.let {

            }
        }.start()
    }


    fun updateOrderDelivered(orderDetails:OrderDetails){
        Thread{
            mUpdateOrderDetails.invoke(orderDetails)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonthlyPreparedDate(monthlyOnce: MonthlyOnce,timeSlot: TimeSlot):String {
        try {
            var text = "Next Delivery On ${DateGenerator.getDayAndMonthForDay(monthlyOnce.dayOfMonth.toString())}"
            return text
        }
        catch (e:Exception){
            println("INT CONVERTION ERROR: $e")
        }
        return ""
    }
    fun getWeeklyPreparedData(weeklyOnce: WeeklyOnce,timeSlot: TimeSlot):String{
        return "Next Delivery this ${days[weeklyOnce.weekId]}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDailyPreparedData(dailySubscription: DailySubscription, timeSlot: TimeSlot):String{
        return checkTimeSlot(timeSlot)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkTimeSlot(timeSlot: TimeSlot):String {
        var currentTime = DateGenerator.getCurrentTime()
        when(timeSlot.timeId){
            0 -> {
                if(currentTime in 6..8){
                    return "Next Delivery Today"
                }
            }
            1 -> {
                if(currentTime in 8..14){
                    return "Next Delivery Today"
                }
            }
            2 -> {
                if(currentTime in 14..18){
                    return "Next Delivery Today"
                }
            }
            3 -> {
                if(currentTime in 18..20){
                    return "Next Delivery Today"
                }
            }
        }
        return "Next Delivery Tomorrow"
    }
}