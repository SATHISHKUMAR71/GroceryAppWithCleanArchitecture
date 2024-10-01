package com.example.shoppinggroceryapp.views.userviews.help

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.help.CustomerRequestEntity

class HelpViewModel(var userDao: UserDao):ViewModel() {
    var productList:MutableLiveData<String> = MutableLiveData()

    fun assignProductList(selectedCartId:Int){
        Thread {
            var cartItems = userDao.getProductsWithCartId(selectedCartId)
            var value = productList.value?:""
            for (i in cartItems) {
                 value += i.productName + " (${i.totalItems}) "
            }
            productList.postValue(value)
        }.start()
    }

    fun sendReq(customerRequestEntity: CustomerRequestEntity){
        Thread{
            userDao.addCustomerRequest(customerRequestEntity)
        }.start()
    }
}