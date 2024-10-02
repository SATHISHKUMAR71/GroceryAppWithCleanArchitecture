package com.example.shoppinggroceryapp.views.userviews.offer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.products.Product
import com.core.usecases.customerusecase.products.GetOfferedProducts
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity

class OfferViewModel(private val mGetOfferedProducts: GetOfferedProducts):ViewModel() {
    var offeredProductEntityList:MutableLiveData<List<Product>> = MutableLiveData()
    fun getOfferedProducts(){
        Thread {
            offeredProductEntityList.postValue(mGetOfferedProducts.invoke())
        }.start()
    }
}