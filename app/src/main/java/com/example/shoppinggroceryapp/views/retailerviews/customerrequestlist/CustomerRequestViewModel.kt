package com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.help.CustomerRequestWithName
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData
import com.core.usecases.cartusecase.getcartusecase.GetProductsWithCartData
import com.core.usecases.helpusecase.GetCustomerReqForSpecificUser
import com.core.usecases.orderusecase.getordersusecase.GetOrderDetailsWithOrderId
import com.core.usecases.helpusecase.GetCustomerRequestWithName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerRequestViewModel(private var mGetCustomerRequestWithName: GetCustomerRequestWithName, private var mGetOrderDetailsWithOrderId: GetOrderDetailsWithOrderId, private val mGetProductsWithCartData: GetProductsWithCartData,private val mGetCustomerReqForSpecificUser: GetCustomerReqForSpecificUser):ViewModel() {


    var customerRequestList:MutableLiveData<List<CustomerRequestWithName>> = MutableLiveData()
    var selectedOrderLiveData:MutableLiveData<OrderDetails> = MutableLiveData()
    var correspondingCartLiveData:MutableLiveData<List<CartWithProductData>> = MutableLiveData()

    fun getCustomerRequest(){
        viewModelScope.launch(Dispatchers.IO) {
            customerRequestList.postValue(mGetCustomerRequestWithName.invoke())
        }
//        Thread {
//            customerRequestList.postValue(mGetCustomerRequestWithName.invoke())
//        }.start()
    }

    fun getSpecificCustomerReq(userId:Int){
        viewModelScope.launch (Dispatchers.IO){
            customerRequestList.postValue(mGetCustomerReqForSpecificUser.invoke(userId))
        }
//        Thread{
//            customerRequestList.postValue(mGetCustomerReqForSpecificUser.invoke(userId))
//        }.start()
    }
    fun getOrderDetails(orderId:Int){
        viewModelScope.launch (Dispatchers.IO) {
            selectedOrderLiveData.postValue(mGetOrderDetailsWithOrderId.invoke(orderId))
        }
//        Thread {
//            selectedOrderLiveData.postValue(mGetOrderDetailsWithOrderId.invoke(orderId))
//        }.start()
    }

    fun getCorrespondingCart(cartId:Int){
        viewModelScope.launch (Dispatchers.IO){
            correspondingCartLiveData.postValue(mGetProductsWithCartData.invoke(cartId))
        }
//        Thread {
//            correspondingCartLiveData.postValue(mGetProductsWithCartData.invoke(cartId))
//        }.start()
    }
}