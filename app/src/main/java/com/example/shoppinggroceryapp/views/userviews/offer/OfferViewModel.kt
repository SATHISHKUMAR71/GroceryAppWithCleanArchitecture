package com.example.shoppinggroceryapp.views.userviews.offer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.products.Product
import com.core.usecases.productusecase.getproductusecase.GetOfferedProducts
import com.example.shoppinggroceryapp.helpers.imagehandlers.SetProductImage
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.sort.ProductSorter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class OfferViewModel(private val mGetOfferedProducts: GetOfferedProducts):ViewModel() {
    var offeredProductEntityList:MutableLiveData<List<Product>> = MutableLiveData()
    fun getOfferedProducts(file:File){
        viewModelScope.launch(Dispatchers.IO) {
            val products = mGetOfferedProducts.invoke()
            offeredProductEntityList.postValue(mGetOfferedProducts.invoke())
//            Thread{
//                if (products != null) {
//                    launch {
//                        for(i in products){
//                            withContext(Dispatchers.IO){
//                                SetProductImage.loadImage(i.mainImage,file)
//                            }
//                        }
//                    }
//                }
//            }.start()

        }
//        Thread {
//            offeredProductEntityList.postValue(mGetOfferedProducts.invoke())
//        }.start()
    }

    fun doSorting(adapter:ProductListAdapter,it:Int,productEntities:List<Product>,sorter: ProductSorter):List<Product>? {
        var newList: List<Product> = mutableListOf()
        if(it==0){
            if(FilterFragment.list==null) {
                newList = sorter.sortByDate(productEntities)
            }
            else{
                newList = sorter.sortByDate(FilterFragment.list!!)
            }
            adapter.setProducts(newList)
        }
        else if(it == 1){
            if(FilterFragment.list==null) {
                newList = sorter.sortByExpiryDate(productEntities)
            }
            else{
                newList = sorter.sortByExpiryDate(FilterFragment.list!!)
            }
            adapter.setProducts(newList)
        }
        else if(it == 2){
            if(FilterFragment.list==null) {
                newList = sorter.sortByDiscount(productEntities)
            }
            else{
                newList = sorter.sortByDiscount(FilterFragment.list!!)
            }
            adapter.setProducts(newList)
        }
        else if(it == 3){
            if(FilterFragment.list==null) {
                newList = sorter.sortByPriceLowToHigh(productEntities)
            }
            else{
                newList = sorter.sortByPriceLowToHigh(FilterFragment.list!!)
            }
            adapter.setProducts(newList)
        }
        else if(it == 4){
            if(FilterFragment.list==null) {
                newList = sorter.sortByPriceHighToLow(productEntities)
            }
            else{
                newList = sorter.sortByPriceHighToLow(FilterFragment.list!!)
            }
            adapter.setProducts(newList)
        }
        if(newList.isNotEmpty()){
            if(FilterFragment.list!=null){
                if(FilterFragment.list!!.size==newList.size){
                    FilterFragment.list = newList.toMutableList()
                }
            }
            return newList
        }
        return null
    }
}