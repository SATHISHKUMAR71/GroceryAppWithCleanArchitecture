package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail

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



    fun getSubscriptionDetails(){
        Thread {
//            for (i in retailerDao.getOrderTimeSlot()) {
//                println("FOR PRODUCTS ORDER ID ${i.orderId} TIME SLOTS: ${i.timeId}")
//            }
//            println("==========")
//            for (i in retailerDao.getDailySubscription()) {
//                println("FOR PRODUCTS ORDER ID ${i.orderId} Daily Subscription ")
//            }
//            println("==========")
//            for (i in retailerDao.getWeeklySubscriptionList()) {
//                println("FOR PRODUCTS ORDER ID ${i.orderId} week id ${i.weekId} Weekly Subscription ")
//            }
//            println("==========")
//            for (i in retailerDao.getMonthlySubscriptionList()) {
//                println("FOR PRODUCTS ORDER ID ${i.orderId} month id ${i.dayOfMonth} Monthly Subscription ")
//            }
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
}