package com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.OrderDetails
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
import com.core.domain.products.CartWithProductData
import com.core.usecases.cartusecase.getcartusecase.GetProductsWithCartData
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddDailySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddMonthlySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddWeeklySubscription
import com.core.usecases.orderusecase.updateorderusecase.UpdateOrderDetails
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.UpdateTimeSlot
import com.core.usecases.orderusecase.getordersusecase.GetSpecificDailyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificMonthlyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificWeeklyOrderWithOrderId
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromDailySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromMonthlySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromWeeklySubscription
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListFragment
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.PaymentFragment
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.TimeSlots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderSummaryViewModel(private val mGetProductsWithCartData: GetProductsWithCartData,
                            private val mUpdateOrderDetails: UpdateOrderDetails,
                            private val mUpdateTimeSlot: UpdateTimeSlot,
                            private val mAddMonthlySubscription: AddMonthlySubscription,
                            private val mAddWeeklySubscription: AddWeeklySubscription,
                            private val mAddDailySubscription: AddDailySubscription,
                            private val mGetSpecificMonthlyOrderWithOrderId: GetSpecificMonthlyOrderWithOrderId,
                            private val mGetSpecificWeeklyOrderWithOrderId: GetSpecificWeeklyOrderWithOrderId,
                            private val mGetSpecificDailyOrderWithOrderId: GetSpecificDailyOrderWithOrderId,
                            private val mRemoveOrderFromDailySubscription: RemoveOrderFromDailySubscription,
                            private val mRemoveOrderFromWeeklySubscription: RemoveOrderFromWeeklySubscription,
                            private val mRemoveOrderFromMonthlySubscription: RemoveOrderFromMonthlySubscription
):ViewModel() {

    var cartItems:MutableLiveData<List<CartWithProductData>> = MutableLiveData()
    var timeIdForOrder:Int = -1
    fun getProductsWithCartId(cartId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            cartItems.postValue(mGetProductsWithCartData.invoke(cartId))
        }
//        Thread{
//            cartItems.postValue(mGetProductsWithCartData.invoke(cartId))
//        }.start()
    }

    fun updateOrderDetails(orderDetails: OrderDetails){
        viewModelScope.launch(Dispatchers.IO) {
            mUpdateOrderDetails.invoke(orderDetails)
        }
//        Thread{
//            mUpdateOrderDetails.invoke(orderDetails)
//        }.start()
    }
    fun updateTimeSlot(timeSlot: TimeSlot){
        viewModelScope.launch(Dispatchers.IO) {
            mUpdateTimeSlot.invoke(timeSlot)
        }
//        Thread{
//            mUpdateTimeSlot.invoke(timeSlot)
//        }.start()
    }
    fun updateMonthly(monthlyOnce: MonthlyOnce){
        viewModelScope.launch(Dispatchers.IO) {
            mAddMonthlySubscription.invoke(monthlyOnce)
            deleteDaily(monthlyOnce.orderId)
            deleteWeekly(monthlyOnce.orderId)
        }
//        Thread{
//            mAddMonthlySubscription.invoke(monthlyOnce)
//            deleteDaily(monthlyOnce.orderId)
//            deleteWeekly(monthlyOnce.orderId)
//        }.start()
    }
    fun deleteMonthly(orderId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            mGetSpecificMonthlyOrderWithOrderId.invoke(orderId)?.let {
                mRemoveOrderFromMonthlySubscription.invoke(it)
            }
        }
//        Thread {
//            mGetSpecificMonthlyOrderWithOrderId.invoke(orderId)?.let {
//                mRemoveOrderFromMonthlySubscription.invoke(it)
//            }
//        }.start()
    }

    fun deleteWeekly(orderId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            mGetSpecificWeeklyOrderWithOrderId.invoke(orderId)?.let {
                mRemoveOrderFromWeeklySubscription.invoke(it)
            }
        }
//        Thread{
//            mGetSpecificWeeklyOrderWithOrderId.invoke(orderId)?.let {
//                mRemoveOrderFromWeeklySubscription.invoke(it)
//            }
//        }.start()
    }
    fun updateDaily(dailySubscription: DailySubscription){
        viewModelScope.launch(Dispatchers.IO) {
            mAddDailySubscription.invoke(dailySubscription)
            deleteWeekly(dailySubscription.orderId)
            deleteMonthly(dailySubscription.orderId)
        }
//        Thread{
//            mAddDailySubscription.invoke(dailySubscription)
//            deleteWeekly(dailySubscription.orderId)
//            deleteMonthly(dailySubscription.orderId)
//        }.start()
    }

    fun updateWeekly(weeklyOnce: WeeklyOnce){
        viewModelScope.launch(Dispatchers.IO) {
            mAddWeeklySubscription.invoke(weeklyOnce)
            deleteDaily(weeklyOnce.orderId)
            deleteMonthly(weeklyOnce.orderId)
        }
//        Thread{
//            mAddWeeklySubscription.invoke(weeklyOnce)
//            deleteDaily(weeklyOnce.orderId)
//            deleteMonthly(weeklyOnce.orderId)
//        }.start()
    }

    fun deleteDaily(orderId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            mGetSpecificDailyOrderWithOrderId.invoke(orderId)?.let {
                mRemoveOrderFromDailySubscription.invoke(it)
            }
        }
//        Thread{
//            mGetSpecificDailyOrderWithOrderId.invoke(orderId)?.let {
//                mRemoveOrderFromDailySubscription.invoke(it)
//            }
//        }.start()
    }

    fun updateSubscription(subscriptionType:String,orderId:Int?,dayOfMonth:Int?,dayOfWeek:Int?){
        when(subscriptionType){
            "Monthly Once" -> {updateMonthly(MonthlyOnce(orderId!!,dayOfMonth!!))}
            "Weekly Once" -> {updateWeekly(WeeklyOnce(orderId!!,dayOfWeek!!))}
            "Daily" -> {updateDaily(DailySubscription(orderId!!))}
            "Once" -> {
                deleteDaily(orderId!!)
                deleteWeekly(orderId)
                deleteMonthly(orderId)
            }
        }
    }



    fun putBundleValuesForOrder(timeSlot: String?,deliveryFrequency:String,dayOfMonth: Int?,dayOfWeek: Int?):Bundle {
        return Bundle().apply {
            putString("deliveryFrequency",deliveryFrequency)
            timeSlot?.let {
                var selectedTimeSlot = ""
                when(it){
                    TimeSlots.EARLY_MORNING.timeDetails -> {
                        timeIdForOrder = 0
                        selectedTimeSlot = TimeSlots.EARLY_MORNING.timeDetails
                    }
                    TimeSlots.MID_MORNING.timeDetails -> {
                        timeIdForOrder = 1
                        selectedTimeSlot = TimeSlots.MID_MORNING.timeDetails
                    }
                    TimeSlots.AFTERNOON.timeDetails -> {
                        timeIdForOrder = 2
                        selectedTimeSlot = TimeSlots.AFTERNOON.timeDetails
                    }
                    TimeSlots.EVENING.timeDetails -> {
                        timeIdForOrder = 3
                        selectedTimeSlot = TimeSlots.EVENING.timeDetails
                    }
                }
                putString("timeSlot", selectedTimeSlot)
                putInt("timeSlotInt",timeIdForOrder)
            }
            dayOfMonth?.let {
                putInt("dayOfMonth",it)
            }
            dayOfWeek?.let {
                putInt("dayOfWeek",it)
            }
        }
    }

    fun getExpectedDeliveryDate(tag:String,s:String):String{
        return when(tag){
            "dayOfWeek" -> {
                "Expected Delivery this $s"
            }

            "dayOfMonth" -> {
                "Expected Delivery on  ${DateGenerator.getDayAndMonthForDay(s)}"
            }
            "daily" -> {
                return "Expected Delivery on ${DateGenerator.getDayAndMonth(DateGenerator.getDeliveryDate())}"
            }
            "once" -> {
                return "Expected Delivery on ${DateGenerator.getDayAndMonth(DateGenerator.getDeliveryDate())}"
            }
            else -> {
                ""
            }
        }
    }

}