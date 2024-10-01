package com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.entity.order.DailySubscriptionEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.MonthlyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.OrderDetailsEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.TimeSlotEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.WeeklyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.CartWithProductDataEntity

class OrderSummaryViewModel(var retailerDao: RetailerDao):ViewModel() {

    var cartItems:MutableLiveData<List<CartWithProductDataEntity>> = MutableLiveData()
    fun getProductsWithCartId(cartId:Int){
        Thread{
            cartItems.postValue(retailerDao.getProductsWithCartId(cartId))
        }.start()
    }

    fun updateOrderDetails(orderDetailsEntity: OrderDetailsEntity){
        Thread{
            retailerDao.updateOrderDetails(orderDetailsEntity)
        }.start()
    }
    fun updateTimeSlot(timeSlotEntity: TimeSlotEntity){
        Thread{
            retailerDao.updateTimeSlot(timeSlotEntity)
        }.start()
    }
    fun updateMonthly(monthlyOnceEntity: MonthlyOnceEntity){
        Thread{
            retailerDao.addMonthlyOnceSubscription(monthlyOnceEntity)
            deleteDaily(monthlyOnceEntity.orderId)
            deleteWeekly(monthlyOnceEntity.orderId)
        }.start()
    }
    fun deleteMonthly(orderId:Int){
        Thread {
            retailerDao.getOrderedDayForMonthlySubscription(
                orderId
            )?.let {
                retailerDao.deleteFromMonthlySubscription(it)
            }
        }.start()
    }

    fun deleteWeekly(orderId: Int){
        Thread{
            retailerDao.getOrderedDayForWeekSubscription(orderId)?.let {
                retailerDao.deleteFromWeeklySubscription(it)
            }
        }.start()
    }
    fun updateDaily(dailySubscriptionEntity: DailySubscriptionEntity){
        Thread{
            retailerDao.addDailySubscription(dailySubscriptionEntity)
            deleteWeekly(dailySubscriptionEntity.orderId)
            deleteMonthly(dailySubscriptionEntity.orderId)
        }.start()
    }

    fun updateWeekly(weeklyOnceEntity: WeeklyOnceEntity){
        Thread{
            retailerDao.addWeeklyOnceSubscription(weeklyOnceEntity)
            deleteDaily(weeklyOnceEntity.orderId)
            deleteMonthly(weeklyOnceEntity.orderId)
        }.start()
    }

    fun deleteDaily(orderId: Int){
        Thread{
            retailerDao.getOrderForDailySubscription(orderId)?.let {
                retailerDao.deleteFromDailySubscription(it)
            }
        }.start()
    }
}