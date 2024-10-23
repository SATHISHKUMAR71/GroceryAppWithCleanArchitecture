package com.example.shoppinggroceryapp.views.userviews.help

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.help.CustomerRequest
import com.core.usecases.cartusecase.getcartusecase.GetProductsWithCartData
import com.core.usecases.helpusecase.AddCustomerRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HelpViewModel(private val mGetProductsWithCartData: GetProductsWithCartData,
                    private val mAddCustomerRequest: AddCustomerRequest
):ViewModel() {
    var productList:MutableLiveData<String> = MutableLiveData()

    fun assignProductList(selectedCartId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            val cartItems = mGetProductsWithCartData.invoke(selectedCartId)
            var value = productList.value?:""
            for (i in cartItems) {
                value += i.productName + " (${i.totalItems}) "
            }
            productList.postValue(value)
        }
//        Thread {
//            val cartItems = mGetProductsWithCartData.invoke(selectedCartId)
//            var value = productList.value?:""
//            for (i in cartItems) {
//                 value += i.productName + " (${i.totalItems}) "
//            }
//            productList.postValue(value)
//        }.start()
    }

    fun sendReq(customerRequest: CustomerRequest){
        viewModelScope.launch(Dispatchers.IO) {
            mAddCustomerRequest.invoke(customerRequest)
        }
//        Thread{
//            mAddCustomerRequest.invoke(customerRequest)
//        }.start()
    }
}