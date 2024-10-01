package com.example.shoppinggroceryapp.views.userviews.offer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity

class OfferViewModel(var userDao: UserDao):ViewModel() {
    var offeredProductEntityList:MutableLiveData<List<ProductEntity>> = MutableLiveData()
    fun getOfferedProducts(){
        Thread {
            offeredProductEntityList.postValue(userDao.getOfferedProducts())
        }.start()
    }
}