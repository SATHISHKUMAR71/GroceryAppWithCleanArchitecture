package com.example.shoppinggroceryapp.views.userviews.offer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.products.Product
import com.core.usecases.productusecase.getproductusecase.GetOfferedProducts

class OfferViewModel(private val mGetOfferedProducts: GetOfferedProducts):ViewModel() {
    var offeredProductEntityList:MutableLiveData<List<Product>> = MutableLiveData()
    fun getOfferedProducts(){
        Thread {
            offeredProductEntityList.postValue(mGetOfferedProducts.invoke())
        }.start()
    }
}