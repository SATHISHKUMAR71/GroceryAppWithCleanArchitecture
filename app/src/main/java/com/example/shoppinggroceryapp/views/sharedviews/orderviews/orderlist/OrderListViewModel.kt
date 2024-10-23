package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    var isDeletedProduct:MutableList<MutableList<Boolean>> = mutableListOf()

    fun getOrdersForSelectedUser(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            orderedItems.postValue(mGetOrderForUser.invoke(userId))
        }
//        Thread {
//            orderedItems.postValue(mGetOrderForUser.invoke(userId))
//        }.start()
    }

    fun getOrdersForSelectedUserWithNoSubscription(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            orderedItems.postValue(mGetOrdersForUserNoSubscription.invoke(userId))
        }
//        Thread {
//            orderedItems.postValue(mGetOrdersForUserNoSubscription.invoke(userId))
//        }.start()
    }
    fun getOrdersForRetailerWithNoSubscription(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            orderedItems.postValue(mGetNormalOrder.invoke())
        }
//        Thread {
//            orderedItems.postValue(mGetNormalOrder.invoke())
//        }.start()
    }

    fun getOrdersForSelectedUserDailySubscription(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            orderedItems.postValue(mGetOrderForUserDailySubscription.invoke(userId))
        }
//        Thread {
//            orderedItems.postValue(mGetOrderForUserDailySubscription.invoke(userId))
//        }.start()
    }

    fun getOrdersForRetailerDailySubscription(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            orderedItems.postValue(mGetDailyOrders.invoke())
        }
//        Thread {
//            orderedItems.postValue(mGetDailyOrders.invoke())
//        }.start()
    }

    fun getOrdersForSelectedUserWeeklySubscription(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            orderedItems.postValue(mGetOrderForUserWeeklySubscription.invoke(userId))
        }
//        Thread {
//            orderedItems.postValue(mGetOrderForUserWeeklySubscription.invoke(userId))
//        }.start()
    }

    fun getOrdersForRetailerWeeklySubscription(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            orderedItems.postValue(mGetWeeklyOrders.invoke())
        }
//        Thread {
//            orderedItems.postValue(mGetWeeklyOrders.invoke())
//        }.start()
    }

    fun getOrdersForSelectedUserMonthlySubscription(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            orderedItems.postValue(mGetOrderForUserMonthlySubscription.invoke(userId))
        }
//        Thread {
//            orderedItems.postValue(mGetOrderForUserMonthlySubscription.invoke(userId))
//        }.start()
    }
    fun getOrdersForRetailerMonthlySubscription(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            orderedItems.postValue(mGetMonthlyOrders.invoke())
        }
//        Thread {
//            orderedItems.postValue(mGetMonthlyOrders.invoke())
//        }.start()
    }

    fun getOrderedItemsForRetailer(){
        viewModelScope.launch(Dispatchers.IO) {
            orderedItems.postValue(mGetAllOrders.invoke())
        }
//        Thread{
//            orderedItems.postValue(mGetAllOrders.invoke())
//        }.start()
    }

    fun getDailySubscriptionDateWithTime(orderId:Int,callback: (Map<DailySubscription,TimeSlot>) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            mGetDailyOrderWithTimeSlot.invoke(orderId)?.let {
                callback(it)
            }
        }
//        Thread{
//            mGetDailyOrderWithTimeSlot.invoke(orderId)?.let {
//                callback(it)
//            }
//        }.start()
    }

    fun getMonthlySubscriptionDateWithTime(orderId:Int,callback: (Map<MonthlyOnce,TimeSlot>) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            mGetSpecificMonthlyOrderWithTimeSlot.invoke(orderId)?.let {
                callback(it)
            }
        }
//        Thread{
//            mGetSpecificMonthlyOrderWithTimeSlot.invoke(orderId)?.let {
//                callback(it)
//            }
//        }.start()
    }

    fun getWeeklySubscriptionDateWithTimeSlot(orderId:Int,callback:(Map<WeeklyOnce,TimeSlot>) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            mGetSpecificWeeklyOrderWithTimeSlot.invoke(orderId)?.let {
                callback(it)
            }
        }
//        Thread{
//            mGetSpecificWeeklyOrderWithTimeSlot.invoke(orderId)?.let {
//                callback(it)
//            }
//        }.start()
    }

    fun getTimeSlot(orderId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            mGetOrderedTimeSlot.invoke(orderId)?.let {

            }
        }
//        Thread{
//            mGetOrderedTimeSlot.invoke(orderId)?.let {
//
//            }
//        }.start()
    }


    fun updateOrderDelivered(orderDetails:OrderDetails){
        viewModelScope.launch(Dispatchers.IO) {
            mUpdateOrderDetails.invoke(orderDetails)
        }
//        Thread{
//            mUpdateOrderDetails.invoke(orderDetails)
//        }.start()
    }

    fun getCartWithProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            for(i in orderedItems.value!!) {
                synchronized(lock) {
                    val isDeletedSubList = mutableListOf<Boolean>()
                    val tmpList = mGetProductsWithCartData.invoke(i.cartId).toMutableList()
                    val tmpDeleteList = mGetDeletedProductsWithCarId.invoke(i.cartId)
                    for(k in tmpList){
                        isDeletedSubList.add(false)
                    }
                    for(k in tmpDeleteList){
                        isDeletedSubList.add(true)
                    }
                    tmpList.addAll(tmpDeleteList)
                    cartWithProductList.value!!.add(
                        tmpList
                    )
                    isDeletedProduct.add(isDeletedSubList)
                }
            }
            dataReady.postValue(true)
        }
//        Thread {
//            for(i in orderedItems.value!!) {
//                synchronized(lock) {
//                    val isDeletedSubList = mutableListOf<Boolean>()
//                    val tmpList = mGetProductsWithCartData.invoke(i.cartId).toMutableList()
//                    val tmpDeleteList = mGetDeletedProductsWithCarId.invoke(i.cartId)
//                    for(k in tmpList){
//                        isDeletedSubList.add(false)
//                    }
//                    for(k in tmpDeleteList){
//                        isDeletedSubList.add(true)
//                    }
//                    tmpList.addAll(tmpDeleteList)
//                    cartWithProductList.value!!.add(
//                        tmpList
//                    )
//                    isDeletedProduct.add(isDeletedSubList)
//                }
//            }
//            dataReady.postValue(true)
//        }.start()
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
    fun getMonthlyPreparedDate(monthlyOnce: MonthlyOnce,timeSlot: TimeSlot,orderedDate: String):String {
        try {
            if(DateGenerator.getCurrentDayOfMonth()==monthlyOnce.dayOfMonth.toString()){
                return if(checkTimeSlot(timeSlot,orderedDate)=="Next Delivery Tomorrow"){
                    "Next Delivery On ${DateGenerator.getNextDayForSpecificMonth(monthlyOnce.dayOfMonth.toString())}"
                } else{
                    checkTimeSlot(timeSlot,orderedDate)
                }
            }
            var text = "Next Delivery On ${DateGenerator.getDayAndMonthForDay(monthlyOnce.dayOfMonth.toString())}"
            return text
        }
        catch (e:Exception){

        }
        return ""
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeeklyPreparedData(weeklyOnce: WeeklyOnce, timeSlot: TimeSlot,orderDate:String):String{
        if(DateGenerator.getCurrentDay()==days[weeklyOnce.weekId]){
            if(checkTimeSlot(timeSlot,orderDate)=="Next Delivery Tomorrow"){
                return "Next Delivery on Next ${days[weeklyOnce.weekId]}"
            }
        }
        return "Next Delivery this ${days[weeklyOnce.weekId]} "
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDailyPreparedData(dailySubscription: DailySubscription, timeSlot: TimeSlot,orderedDate:String):String{
        return checkTimeSlot(timeSlot,orderedDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkTimeSlot(timeSlot: TimeSlot, orderedDate: String):String {
        var currentTime = DateGenerator.getCurrentTime()

        when(timeSlot.timeId){
            0 -> {
                if(currentTime in 6..8 && orderedDate!=DateGenerator.getCurrentDate()) {
                    return "Next Delivery Today"
                }
                else if(currentTime<8){
                    if (orderedDate!=DateGenerator.getCurrentDate()) {
                        return "Next Delivery Today"
                    }
                }
            }
            1 -> {
                if(currentTime in 8..14&& orderedDate!=DateGenerator.getCurrentDate()) {
                    return "Next Delivery Today"
                }
                else if(currentTime<14){
                    if (orderedDate!=DateGenerator.getCurrentDate()) {
                        return "Next Delivery Today"
                    }
                }
            }
            2 -> {
                if(currentTime in 14..18&& orderedDate!=DateGenerator.getCurrentDate()) {
                    return "Next Delivery Today"
                }
                else if(currentTime<18){
                    if (orderedDate!=DateGenerator.getCurrentDate()) {
                        return "Next Delivery Today"
                    }
                }
            }
            3 -> {
                if(currentTime in 18..20&& orderedDate!=DateGenerator.getCurrentDate()) {
                    return "Next Delivery Today"
                }
                else if(currentTime<20){
                    if (orderedDate!=DateGenerator.getCurrentDate()) {
                        return "Next Delivery Today"
                    }
                }
            }
        }
        return "Next Delivery Tomorrow"
    }

}