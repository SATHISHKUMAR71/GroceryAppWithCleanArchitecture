package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.order.OrderDetails
import com.core.domain.user.Address
import com.core.usecases.addressusecase.GetSpecificAddress
import com.core.usecases.orderusecase.updateorderusecase.UpdateOrderDetails
import com.core.usecases.orderusecase.getordersusecase.GetOrderedTimeSlot
import com.core.usecases.orderusecase.getordersusecase.GetSpecificDailyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificMonthlyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificWeeklyOrderWithOrderId
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromDailySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromMonthlySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromWeeklySubscription
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListFragment

class OrderDetailViewModel(private var mUpdateOrderDetails: UpdateOrderDetails,
                           private val mGetSpecificAddress: GetSpecificAddress,
                           private val mGetSpecificDailyOrderWithOrderId: GetSpecificDailyOrderWithOrderId,
                           private val mGetSpecificMonthlyOrderWithOrderId: GetSpecificMonthlyOrderWithOrderId,
                           private val mGetSpecificWeeklyOrderWithOrderId: GetSpecificWeeklyOrderWithOrderId,
                           private val mRemoveOrderFromMonthlySubscription: RemoveOrderFromMonthlySubscription,
                           private val mRemoveOrderFromDailySubscription: RemoveOrderFromDailySubscription,
                           private val mRemoveOrderFromWeeklySubscription: RemoveOrderFromWeeklySubscription,
                           private val mGetOrderedTimeSlot: GetOrderedTimeSlot
): ViewModel() {
    var groceriesArrivingToday = "Groceries Arriving Today"
    var selectedAddress:MutableLiveData<Address> = MutableLiveData()
    var date:MutableLiveData<Int> = MutableLiveData()
    var timeSlot:MutableLiveData<Int> = MutableLiveData()
    fun updateOrderDetails(orderDetails: OrderDetails){
        Thread{
            mUpdateOrderDetails.invoke(orderDetails)
        }.start()
    }

    fun getSelectedAddress(addressId:Int){
        Thread{
            selectedAddress.postValue(mGetSpecificAddress.invoke(addressId))
        }.start()
    }


    fun deleteMonthly(orderId:Int){
        Thread {
            mGetSpecificMonthlyOrderWithOrderId.invoke(orderId)?.let {
                mRemoveOrderFromMonthlySubscription.invoke(it)
            }
        }.start()
    }

    fun deleteWeekly(orderId: Int){
        Thread{
            mGetSpecificWeeklyOrderWithOrderId.invoke(orderId)?.let {
                mRemoveOrderFromWeeklySubscription.invoke(it)
            }
        }.start()
    }

    fun deleteDaily(orderId: Int){
        Thread{
            mGetSpecificDailyOrderWithOrderId.invoke(orderId)?.let {
                mRemoveOrderFromDailySubscription.invoke(it)
            }
        }.start()
    }


    fun getMonthlySubscriptionDate(orderId:Int){
        Thread{
            mGetSpecificMonthlyOrderWithOrderId.invoke(orderId)?.let {
                date.postValue(it.dayOfMonth)
            }
        }.start()
    }

    fun getWeeklySubscriptionDate(orderId:Int){
        Thread{
            mGetSpecificWeeklyOrderWithOrderId.invoke(orderId)?.let {
                date.postValue(it.weekId)
            }
        }.start()
    }

    fun getTimeSlot(orderId: Int){
        Thread{
            mGetOrderedTimeSlot.invoke(orderId)?.let {
                timeSlot.postValue(it.timeId)
            }
        }.start()
    }

    fun assignText(timeSlot:Int,currentTime:Int):String{
        var text =""
        when(timeSlot){
            0 -> {
                if (currentTime in 6..8) {
                    text = groceriesArrivingToday
                }
            }
            1 -> {
                if(currentTime in 8..14){
                    text = groceriesArrivingToday
                }
            }
            2 -> {
                if (currentTime in 14..18) {
                    text = groceriesArrivingToday
                }
            }
            3 -> {
                if (currentTime in 18..20) {
                    text = groceriesArrivingToday
                }
            }
        }
        return text
    }


    fun assignDeliveryStatus(deliveryDate:String?):String?{

        return if(OrderListFragment.selectedOrder!!.deliveryStatus=="Delivered"){
            "Delivered on ${DateGenerator.getDayAndMonth(deliveryDate?: DateGenerator.getDeliveryDate())}"
        } else if((OrderListFragment.selectedOrder!!.deliveryStatus=="Pending")){
            "Delivery Expected on:  ${DateGenerator.getDayAndMonth(deliveryDate?: DateGenerator.getDeliveryDate())}"
        } else if(OrderListFragment.selectedOrder!!.deliveryStatus == "Delayed"){
            "Delivery Expected on:  ${DateGenerator.getDayAndMonth(DateGenerator.getDeliveryDate())}"
        } else if(OrderListFragment.selectedOrder!!.deliveryStatus== "Cancelled"){
            null
        } else{
            "Delivery Expected on:  ${DateGenerator.getDayAndMonth(DateGenerator.getDeliveryDate())}"
        }
    }
}