package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData
import com.core.domain.products.Product
import com.core.domain.user.Address
import com.core.usecases.addressusecase.GetSpecificAddress
import com.core.usecases.orderusecase.updateorderusecase.UpdateOrderDetails
import com.core.usecases.orderusecase.getordersusecase.GetOrderedTimeSlot
import com.core.usecases.orderusecase.getordersusecase.GetSpecificDailyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificMonthlyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificWeeklyOrderWithOrderId
import com.core.usecases.productusecase.getproductusecase.GetProductsById
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.UpdateProduct
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromDailySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromMonthlySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromWeeklySubscription
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderDetailViewModel(private var mUpdateOrderDetails: UpdateOrderDetails,
                           private val mGetSpecificAddress: GetSpecificAddress,
                           private val mGetSpecificDailyOrderWithOrderId: GetSpecificDailyOrderWithOrderId,
                           private val mGetSpecificMonthlyOrderWithOrderId: GetSpecificMonthlyOrderWithOrderId,
                           private val mGetSpecificWeeklyOrderWithOrderId: GetSpecificWeeklyOrderWithOrderId,
                           private val mRemoveOrderFromMonthlySubscription: RemoveOrderFromMonthlySubscription,
                           private val mRemoveOrderFromDailySubscription: RemoveOrderFromDailySubscription,
                           private val mRemoveOrderFromWeeklySubscription: RemoveOrderFromWeeklySubscription,
                           private val mGetProductById:GetProductsById,
                           private val mUpdateProduct: UpdateProduct,
                           private val mGetOrderedTimeSlot: GetOrderedTimeSlot
): ViewModel() {
    var groceriesArrivingToday = "Groceries Arriving Today"
    var selectedOrderProduct:MutableLiveData<Product> = MutableLiveData()
    var selectedOrderWithProductData = mutableListOf<CartWithProductData>()
    var selectedAddress:MutableLiveData<Address> = MutableLiveData()
    var date:MutableLiveData<Int> = MutableLiveData()
    var timeSlot:MutableLiveData<Int> = MutableLiveData()
    fun updateOrderDetails(orderDetails: OrderDetails){
        viewModelScope.launch(Dispatchers.IO) {
            println("9812 $selectedOrderWithProductData")
            for(i in selectedOrderWithProductData){
                mGetProductById.invoke(i.productId)?.let {
                    val newProd = it.copy(availableItems = it.availableItems+i.totalItems)
                    mUpdateProduct.invoke(newProd)
                    println("9812 NEW PRODUCT DETAILS: $newProd")
                }
            }
            mUpdateOrderDetails.invoke(orderDetails)
        }
//        Thread{
//            println("9812 $selectedOrderWithProductData")
//            for(i in selectedOrderWithProductData){
//                mGetProductById.invoke(i.productId)?.let {
//                    val newProd = it.copy(availableItems = it.availableItems+i.totalItems)
//                    mUpdateProduct.invoke(newProd)
//                    println("9812 NEW PRODUCT DETAILS: $newProd")
//                }
//            }
//            mUpdateOrderDetails.invoke(orderDetails)
//        }.start()
    }

    fun getSelectedAddress(addressId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            selectedAddress.postValue(mGetSpecificAddress.invoke(addressId))
        }
//        Thread{
//            selectedAddress.postValue(mGetSpecificAddress.invoke(addressId))
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


    fun getMonthlySubscriptionDate(orderId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            mGetSpecificMonthlyOrderWithOrderId.invoke(orderId)?.let {
                date.postValue(it.dayOfMonth)
            }
        }
//        Thread{
//            mGetSpecificMonthlyOrderWithOrderId.invoke(orderId)?.let {
//                date.postValue(it.dayOfMonth)
//            }
//        }.start()
    }

    fun getWeeklySubscriptionDate(orderId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            mGetSpecificWeeklyOrderWithOrderId.invoke(orderId)?.let {
                date.postValue(it.weekId)
            }
        }
//        Thread{
//            mGetSpecificWeeklyOrderWithOrderId.invoke(orderId)?.let {
//                date.postValue(it.weekId)
//            }
//        }.start()
    }

    fun getTimeSlot(orderId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            mGetOrderedTimeSlot.invoke(orderId)?.let {
                timeSlot.postValue(it.timeId)
            }
        }
//        Thread{
//            mGetOrderedTimeSlot.invoke(orderId)?.let {
//                timeSlot.postValue(it.timeId)
//            }
//        }.start()
    }

    fun assignText(timeSlot:Int,currentTime:Int,selectedOrder:OrderDetails?):String?{
        var text =""
        when(timeSlot){
            0 -> {
                if (currentTime in 6..8) {
                    text = groceriesArrivingToday
                }
                else if(currentTime<6){
                    if (selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                        return "Next Delivery Today"
                    }
                }
                else{
                    return null
                }
            }
            1 -> {
                if(currentTime in 8..14){
                    text = groceriesArrivingToday
                }
                else if(currentTime<8){
                    if (selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                        return "Next Delivery Today"
                    }
                }
                else{
                    return null
                }
            }
            2 -> {
                if (currentTime in 14..18) {
                    text = groceriesArrivingToday
                }
                else if(currentTime<14){
                    if (selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                        return "Next Delivery Today"
                    }
                }
                else{
                    return null
                }
            }
            3 -> {
                if (currentTime in 18..20) {
                    text = groceriesArrivingToday
                }
                else if(currentTime<18){
                    if (selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                        return "Next Delivery Today"
                    }
                }
                else{
                    return null
                }
            }
        }
        return text
    }


    fun getProductById(productId:Long){
        viewModelScope.launch(Dispatchers.IO) {
            selectedOrderProduct.postValue(mGetProductById.invoke(productId))
        }
//        Thread{
//            selectedOrderProduct.postValue(mGetProductById.invoke(productId))
//        }.start()
    }
    fun assignDeliveryStatus(deliveryDate:String?,selectedOrder: OrderDetails?):String?{

        return if(selectedOrder!!.deliveryStatus=="Delivered"){
            "Delivered on ${DateGenerator.getDayAndMonth(deliveryDate?: DateGenerator.getDeliveryDate())}"
        } else if((selectedOrder!!.deliveryStatus=="Pending")){
            "Delivery Expected on:  ${DateGenerator.getDayAndMonth(deliveryDate?: DateGenerator.getDeliveryDate())}"
        } else if(selectedOrder!!.deliveryStatus == "Delayed"){
            "Delivery Expected on:  ${DateGenerator.getDayAndMonth(DateGenerator.getDeliveryDate())}"
        } else if(selectedOrder!!.deliveryStatus== "Cancelled"){
            null
        } else{
            "Delivery Expected on:  ${DateGenerator.getDayAndMonth(DateGenerator.getDeliveryDate())}"
        }
    }
}