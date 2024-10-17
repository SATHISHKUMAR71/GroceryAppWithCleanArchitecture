package com.example.shoppinggroceryapp.views.sharedviews.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.products.Product
import com.core.usecases.filterusecases.GetAllBrands
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity

class FilterViewModel(var mGetAllBrands: GetAllBrands):ViewModel() {

    var brandList:MutableLiveData<List<String>> = MutableLiveData()
    fun getBrandNames(){
        Thread {
            brandList.postValue(mGetAllBrands.invoke())
        }.start()
    }

    fun filterList(productEntityList:List<Product>, offer:Float):List<Product>{
        return productEntityList.filter { it.offer>=offer }
    }

    fun filterListBelow(productEntityList:List<Product>, offer:Float):List<Product>{
        return productEntityList.filter { it.offer<offer }
    }

}