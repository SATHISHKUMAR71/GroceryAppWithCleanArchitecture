package com.example.shoppinggroceryapp.views.userviews.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.framework.db.dao.ProductDao
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity

class HomeViewModel(var productDao: ProductDao):ViewModel() {

    var recentlyViewedList:MutableLiveData<MutableList<ProductEntity>> = MutableLiveData()
    fun getRecentlyViewedItems(){
        Thread{
            val list = mutableListOf<ProductEntity>()
            val recentlyViewedProduct = productDao.getRecentlyViewedProducts(MainActivity.userId.toInt())
            for(i in recentlyViewedProduct){
                var productEntity:ProductEntity? = productDao.getProductById(i.toLong())
                productEntity?.let {
                    list.add(it)
                }
            }
            list.reverse()
            recentlyViewedList.postValue(list)
        }.start()
    }
}