package com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersuccess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.order.Cart
import com.core.domain.order.CartMapping
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.OrderDetails
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
import com.core.domain.products.CartWithProductData
import com.core.domain.products.Product
import com.core.usecases.cartusecase.setcartusecase.AddCartForUser
import com.core.usecases.cartusecase.getcartusecase.GetCartForUser
import com.core.usecases.cartusecase.getcartusecase.GetCartItems
import com.core.usecases.cartusecase.setcartusecase.UpdateCart
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddDailySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddMonthlySubscription
import com.core.usecases.orderusecase.updateorderusecase.AddOrder
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddTimeSlot
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddWeeklySubscription
import com.core.usecases.orderusecase.getordersusecase.GetOrderWithProductsByOrderId
import com.core.usecases.productusecase.getproductusecase.GetProductsById
import com.core.usecases.productusecase.setproductusecase.UpdateAvailableProducts
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.framework.db.entity.order.OrderDetailsEntity

class OrderSuccessViewModel(private val mAddOrder: AddOrder,
                            private val mGetOrderWithProductsByOrderId: GetOrderWithProductsByOrderId,
                            private val mAddMonthlySubscription: AddMonthlySubscription,
                            private val mAddWeeklySubscription: AddWeeklySubscription,
                            private val mUpdateAvailableProducts: UpdateAvailableProducts,
                            private val mAddDailySubscription: AddDailySubscription,
                            private val mGetProductsById: GetProductsById,
                            private val mAddTimeSlot: AddTimeSlot,
                            private val mUpdateCart: UpdateCart,
                            private val mAddCartForUser: AddCartForUser,
                            private val mGetCartForUser: GetCartForUser,
                            private val mGetCartItems: GetCartItems
):ViewModel() {

    val lock = Any()
    var gotOrder: OrderDetailsEntity? = null
    var oldCartId:Int = -1
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

    fun updateProductDetails(){
        Thread{
            for(i in mGetCartItems.invoke(oldCartId)){
                mGetProductsById.invoke(i.productId.toLong())?.let {
                    println("32145 BEFORE UPDATING PRODUCT: $it")
                    mUpdateAvailableProducts.invoke(it.copy(availableItems = it.availableItems-i.totalItems))
                    println("32145 AFTER UPDATING PRODUCT: ${it.copy(availableItems = it.availableItems-i.totalItems)}")
                }
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