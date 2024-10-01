package com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersuccess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.entity.order.CartMappingEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.DailySubscriptionEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.MonthlyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.OrderDetailsEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.TimeSlotEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.WeeklyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.CartWithProductDataEntity
class OrderSuccessViewModel(var retailerDao: RetailerDao):ViewModel() {
    val lock = Any()
    var gotOrder: OrderDetailsEntity? = null
    var orderedId:MutableLiveData<Long> = MutableLiveData()
    var cartItems:List<CartWithProductDataEntity>? = null
    var newCart:MutableLiveData<CartMappingEntity> = MutableLiveData()
    var orderWithCart:MutableLiveData<Map<OrderDetailsEntity,List<CartWithProductDataEntity>>> = MutableLiveData()
    fun placeOrder(cartId:Int,paymentMode:String,addressId:Int,deliveryStatus:String,paymentStatus:String,deliveryFrequency:String){
        Thread {
            synchronized(lock) {
                orderedId.postValue(retailerDao.addOrder(
                    OrderDetailsEntity(
                        0,
                        orderedDate = DateGenerator.getCurrentDate(),
                        deliveryDate = DateGenerator.getDeliveryDate(),
                        cartId = cartId,
                        paymentMode = paymentMode,
                        paymentStatus = paymentStatus,
                        addressId = addressId,
                        deliveryStatus = deliveryStatus,
                        deliveryFrequency = deliveryFrequency
                    )))
            }
        }.start()
    }

    fun getOrderAndCorrespondingCart(cartId:Int){
        Thread {
            synchronized(lock) {
                println(retailerDao.getOrder(cartId))
                orderWithCart.postValue(retailerDao.getOrderWithProductsWithOrderId(cartId))
            }

        }.start()
    }

    fun addMonthlySubscription(monthlyOnceEntity: MonthlyOnceEntity){
        Thread{
            retailerDao.addMonthlyOnceSubscription(monthlyOnceEntity)
            getSubscriptionDetails()
        }.start()
    }

    fun addWeeklySubscription(weeklyOnceEntity: WeeklyOnceEntity){
        Thread{
            retailerDao.addWeeklyOnceSubscription(weeklyOnceEntity)
            getSubscriptionDetails()
        }.start()
    }

    fun addDailySubscription(dailySubscriptionEntity: DailySubscriptionEntity){
        Thread{
            retailerDao.addDailySubscription(dailySubscriptionEntity)
            getSubscriptionDetails()
        }.start()
    }

    fun addOrderToTimeSlot(timeSlotEntity: TimeSlotEntity){
        Thread{
            retailerDao.addTimeSlot(timeSlotEntity)
        }.start()
    }

    fun getSubscriptionDetails(){
        for(i in retailerDao.getOrderTimeSlot()){
            println("FOR PRODUCTS ORDER ID ${i.orderId} TIME SLOTS: ${i.timeId}")
        }
        println("==========")
        for(i in retailerDao.getDailySubscription()){
            println("FOR PRODUCTS ORDER ID ${i.orderId} Daily Subscription ")
        }
        println("==========")
        for(i in retailerDao.getWeeklySubscriptionList()){
            println("FOR PRODUCTS ORDER ID ${i.orderId} week id ${i.weekId} Weekly Subscription ")
        }
        println("==========")
        for(i in retailerDao.getMonthlySubscriptionList()){
            println("FOR PRODUCTS ORDER ID ${i.orderId} month id ${i.dayOfMonth} Monthly Subscription ")
        }
    }
    fun updateAndAssignNewCart(cartId: Int,userId:Int){
        Thread {
            synchronized(lock) {
                retailerDao.updateCartMapping(CartMappingEntity(cartId, userId, "not available"))
                retailerDao.addCartForUser(CartMappingEntity(0, userId, "available"))
                newCart.postValue(retailerDao.getCartForUser(userId))
            }
        }.start()
    }
}