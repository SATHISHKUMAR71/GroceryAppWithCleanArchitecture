package com.example.shoppinggroceryapp.views.userviews.help

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.help.CustomerRequest
import com.core.usecases.cartusecase.getcartusecase.GetProductsWithCartData
import com.core.usecases.helpusecase.AddCustomerRequest

class HelpViewModel(private val mGetProductsWithCartData: GetProductsWithCartData,
                    private val mAddCustomerRequest: AddCustomerRequest
):ViewModel() {
    var productList:MutableLiveData<String> = MutableLiveData()

    fun assignProductList(selectedCartId:Int){
        Thread {
            val cartItems = mGetProductsWithCartData.invoke(selectedCartId)
            var value = productList.value?:""
            for (i in cartItems) {
                 value += i.productName + " (${i.totalItems}) "
            }
            productList.postValue(value)
        }.start()
    }

    fun sendReq(customerRequest: CustomerRequest){
        Thread{
            mAddCustomerRequest.invoke(customerRequest)
        }.start()
    }
}