package com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    fun getProductsWithCartId(cartId:Int){
        Thread{
            cartItems.postValue(mGetProductsWithCartData.invoke(cartId))
        }.start()
    }

    fun updateOrderDetails(orderDetails: OrderDetails){
        Thread{
            mUpdateOrderDetails.invoke(orderDetails)
        }.start()
    }
    fun updateTimeSlot(timeSlot: TimeSlot){
        Thread{
            mUpdateTimeSlot.invoke(timeSlot)
        }.start()
    }
    fun updateMonthly(monthlyOnce: MonthlyOnce){
        Thread{
            mAddMonthlySubscription.invoke(monthlyOnce)
            deleteDaily(monthlyOnce.orderId)
            deleteWeekly(monthlyOnce.orderId)
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
    fun updateDaily(dailySubscription: DailySubscription){
        Thread{
            mAddDailySubscription.invoke(dailySubscription)
            deleteWeekly(dailySubscription.orderId)
            deleteMonthly(dailySubscription.orderId)
        }.start()
    }

    fun updateWeekly(weeklyOnce: WeeklyOnce){
        Thread{
            mAddWeeklySubscription.invoke(weeklyOnce)
            deleteDaily(weeklyOnce.orderId)
            deleteMonthly(weeklyOnce.orderId)
        }.start()
    }

    fun deleteDaily(orderId: Int){
        Thread{
            mGetSpecificDailyOrderWithOrderId.invoke(orderId)?.let {
                mRemoveOrderFromDailySubscription.invoke(it)
            }
        }.start()
    }
}