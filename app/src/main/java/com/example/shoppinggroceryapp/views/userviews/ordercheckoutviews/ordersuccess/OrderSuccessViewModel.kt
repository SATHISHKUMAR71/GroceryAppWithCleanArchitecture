package com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersuccess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.order.CartMapping
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.OrderDetails
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
import com.core.domain.products.CartWithProductData
import com.core.usecases.cartusecase.setcartusecase.AddCartForUser
import com.core.usecases.cartusecase.getcartusecase.GetCartForUser
import com.core.usecases.cartusecase.setcartusecase.UpdateCart
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddDailySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddMonthlySubscription
import com.core.usecases.orderusecase.updateorderusecase.AddOrder
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddTimeSlot
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddWeeklySubscription
import com.core.usecases.orderusecase.getordersusecase.GetOrderWithProductsByOrderId
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.framework.db.entity.order.OrderDetailsEntity

class OrderSuccessViewModel(private val mAddOrder: AddOrder,
                            private val mGetOrderWithProductsByOrderId: GetOrderWithProductsByOrderId,
                            private val mAddMonthlySubscription: AddMonthlySubscription,
                            private val mAddWeeklySubscription: AddWeeklySubscription,
                            private val mAddDailySubscription: AddDailySubscription,
                            private val mAddTimeSlot: AddTimeSlot,
                            private val mUpdateCart: UpdateCart,
                            private val mAddCartForUser: AddCartForUser,
                            private val mGetCartForUser: GetCartForUser
):ViewModel() {

    val lock = Any()
    var gotOrder: OrderDetailsEntity? = null
    var orderedId:MutableLiveData<Long> = MutableLiveData()
    var newCart:MutableLiveData<CartMapping> = MutableLiveData()
    var orderWithCart:MutableLiveData<Map<OrderDetails,List<CartWithProductData>>> = MutableLiveData()
    fun placeOrder(cartId:Int,paymentMode:String,addressId:Int,deliveryStatus:String,paymentStatus:String,deliveryFrequency:String){
        Thread {
            synchronized(lock) {
                orderedId.postValue(mAddOrder.invoke(
                    OrderDetails(0,
                        orderedDate = DateGenerator.getCurrentDate(),
                        deliveryDate = DateGenerator.getDeliveryDate(),
                        cartId = cartId,
                        paymentMode = paymentMode,
                        paymentStatus = paymentStatus,
                        addressId = addressId,
                        deliveryStatus = deliveryStatus,
                        deliveryFrequency = deliveryFrequency)))
            }
        }.start()
    }

    fun getOrderAndCorrespondingCart(orderId:Int){
        Thread {
            synchronized(lock) {
                orderWithCart.postValue(mGetOrderWithProductsByOrderId.invoke(orderId))
            }

        }.start()
    }

    fun addMonthlySubscription(monthlyOnce: MonthlyOnce){
        Thread{
            mAddMonthlySubscription.invoke(monthlyOnce)
            getSubscriptionDetails()
        }.start()
    }

    fun addWeeklySubscription(weeklyOnce: WeeklyOnce){
        Thread{
            mAddWeeklySubscription.invoke(weeklyOnce)
            getSubscriptionDetails()
        }.start()
    }

    fun addDailySubscription(dailySubscription: DailySubscription){
        Thread{
            mAddDailySubscription.invoke(dailySubscription)
            getSubscriptionDetails()
        }.start()
    }

    fun addOrderToTimeSlot(timeSlot: TimeSlot){
        Thread{
            mAddTimeSlot.invoke(timeSlot)
        }.start()
    }

    fun getSubscriptionDetails(){
//        for(i in retailerDao.getOrderTimeSlot()){
//            println("FOR PRODUCTS ORDER ID ${i.orderId} TIME SLOTS: ${i.timeId}")
//        }
//        println("==========")
//        for(i in retailerDao.getDailySubscription()){
//            println("FOR PRODUCTS ORDER ID ${i.orderId} Daily Subscription ")
//        }
//        println("==========")
//        for(i in retailerDao.getWeeklySubscriptionList()){
//            println("FOR PRODUCTS ORDER ID ${i.orderId} week id ${i.weekId} Weekly Subscription ")
//        }
//        println("==========")
//        for(i in retailerDao.getMonthlySubscriptionList()){
//            println("FOR PRODUCTS ORDER ID ${i.orderId} month id ${i.dayOfMonth} Monthly Subscription ")
//        }
    }
    fun updateAndAssignNewCart(cartId: Int,userId:Int){
        Thread {
            synchronized(lock) {
                mUpdateCart.invoke(CartMapping(cartId, userId, "not available"))
                mAddCartForUser.invoke(CartMapping(0, userId, "available"))
                newCart.postValue(mGetCartForUser.invoke(userId))
            }
        }.start()
    }
}