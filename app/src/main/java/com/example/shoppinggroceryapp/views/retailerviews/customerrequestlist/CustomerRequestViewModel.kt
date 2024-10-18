package com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.help.CustomerRequestWithName
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData
import com.core.usecases.cartusecase.getcartusecase.GetProductsWithCartData
import com.core.usecases.helpusecase.GetCustomerReqForSpecificUser
import com.core.usecases.orderusecase.getordersusecase.GetOrderDetailsWithOrderId
import com.core.usecases.helpusecase.GetCustomerRequestWithName

class CustomerRequestViewModel(private var mGetCustomerRequestWithName: GetCustomerRequestWithName, private var mGetOrderDetailsWithOrderId: GetOrderDetailsWithOrderId, private val mGetProductsWithCartData: GetProductsWithCartData,private val mGetCustomerReqForSpecificUser: GetCustomerReqForSpecificUser):ViewModel() {


    var customerRequestList:MutableLiveData<List<CustomerRequestWithName>> = MutableLiveData()
    var selectedOrderLiveData:MutableLiveData<OrderDetails> = MutableLiveData()
    var correspondingCartLiveData:MutableLiveData<List<CartWithProductData>> = MutableLiveData()

    fun getCustomerRequest(){
        Thread {
            customerRequestList.postValue(mGetCustomerRequestWithName.invoke())
        }.start()
    }

    fun getSpecificCustomerReq(userId:Int){
        Thread{
            customerRequestList.postValue(mGetCustomerReqForSpecificUser.invoke(userId))
        }.start()
    }
    fun getOrderDetails(orderId:Int){
        Thread {
            selectedOrderLiveData.postValue(mGetOrderDetailsWithOrderId.invoke(orderId))
        }.start()
    }

    fun getCorrespondingCart(cartId:Int){
        Thread {
            correspondingCartLiveData.postValue(mGetProductsWithCartData.invoke(cartId))
        }.start()
    }
}