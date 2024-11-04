package com.example.shoppinggroceryapp.views.sharedviews.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.products.BrandData
import com.core.domain.products.Product
import com.core.usecases.filterusecases.GetAllBrands
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max

class FilterViewModel(var mGetAllBrands: GetAllBrands):ViewModel() {
    
    var brandList:MutableLiveData<List<String>> = MutableLiveData()
    var brandMap:List<BrandData> = mutableListOf()
    fun getBrandNames(){
        viewModelScope.launch (Dispatchers.IO){
            brandList.postValue(mGetAllBrands.invoke().map { it.brandName })
        }
//        Thread {
//            brandList.postValue(mGetAllBrands.invoke().map { it.brandName })
//        }.start()
    }

    fun filterList(productEntityList:List<Product>, offer:Float):List<Product>{
        return productEntityList.filter { it.offer>=offer }
    }


    fun doFilter(productEntityList: List<Product>):List<Product>{
        var list:List<Product> = productEntityList
        if(FilterExpiry.startExpiryDate.isNotEmpty()){
            list = list.filter { it.expiryDate>=FilterExpiry.startExpiryDate }
        }
        if(FilterExpiry.endExpiryDate.isNotEmpty()){
            list = list.filter { it.expiryDate<=FilterExpiry.endExpiryDate }
        }
        if(FilterExpiry.startManufactureDate.isNotEmpty()){
            list = list.filter { it.manufactureDate>=FilterExpiry.startManufactureDate }
        }
        if(FilterExpiry.endManufactureDate.isNotEmpty()){
            list = list.filter { it.manufactureDate<=FilterExpiry.endManufactureDate }
        }
        list = list.filter { ((it.price) - ((it.offer/100) * it.price))>=FilterPrice.priceStartFrom }
        if(FilterPrice.priceEndTo<=FilterPrice.MAX_PRICE_VALUE){
            list = list.filter { ((it.price) - ((it.offer/100) * it.price))<=FilterPrice.priceEndTo }
        }
        brandMap = (mGetAllBrands.invoke())
        if(FilterFragmentSearch.checkedList.isNotEmpty()) {
            var tmpBrandData =
                brandMap.filter { it.brandName in FilterFragmentSearch.checkedList }
            var tmpBrandIds = tmpBrandData.map { it.brandId }
            tmpBrandIds.let { brandIdList ->
                list = list.filter { it.brandId in brandIdList }
            }
        }
        if(FilterFragmentSearch.checkedDiscountList.isNotEmpty()){
            list = list.filter { it.offer >= FilterFragmentSearch.checkedDiscountList.min() }
        }
        return list
    }

    fun filterListBelow(productEntityList:List<Product>, offer:Float):List<Product>{
        return productEntityList.filter { it.offer<offer }
    }

}