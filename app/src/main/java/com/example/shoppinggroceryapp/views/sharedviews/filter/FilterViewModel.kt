package com.example.shoppinggroceryapp.views.sharedviews.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.products.BrandData
import com.core.domain.products.Product
import com.core.usecases.filterusecases.GetAllBrands
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity

class FilterViewModel(var mGetAllBrands: GetAllBrands):ViewModel() {

    var brandList:MutableLiveData<List<String>> = MutableLiveData()
    var brandMap:List<BrandData> = mutableListOf()
    fun getBrandNames(){
        Thread {
            brandList.postValue(mGetAllBrands.invoke().map { it.brandName })
        }.start()
    }

    fun filterList(productEntityList:List<Product>, offer:Float):List<Product>{
        return productEntityList.filter { it.offer>=offer }
    }


    fun doFilter(productEntityList: List<Product>):List<Product>{
        println("432456 BRAND CHECK LIST: ${FilterFragmentSearch.checkedList} DISCOUNT CHECK LIST: ${FilterFragmentSearch.checkedDiscountList}")
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
        if(FilterPrice.priceEndTo<=2000F){
            list = list.filter { ((it.price) - ((it.offer/100) * it.price))<=FilterPrice.priceEndTo }
        }
        println("78978 LIST SIZE before IN 46: ${list.size}")
        brandMap = (mGetAllBrands.invoke())
        if(FilterFragmentSearch.checkedList.isNotEmpty()) {
            var tmpBrandData =
                brandMap.filter { it.brandName in FilterFragmentSearch.checkedList }
            var tmpBrandIds = tmpBrandData.map { it.brandId }
            tmpBrandIds.let { brandIdList ->
                list = list.filter { it.brandId in brandIdList }
            }
            println("78978 LIST SIZE on is empty IN 46 :${FilterFragmentSearch.checkedList} ${list.size} $tmpBrandIds $tmpBrandData ${brandMap}")
        }
        if(FilterFragmentSearch.checkedDiscountList.isNotEmpty()){
            list = list.filter { it.offer in FilterFragmentSearch.checkedDiscountList }
        }
        return list
    }

    fun filterListBelow(productEntityList:List<Product>, offer:Float):List<Product>{
        return productEntityList.filter { it.offer<offer }
    }

}