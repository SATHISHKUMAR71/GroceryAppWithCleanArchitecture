package com.example.shoppinggroceryapp.views.userviews.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.products.Product
import com.core.usecases.productusecase.getproductusecase.GetProductsById
import com.core.usecases.productusecase.getproductusecase.GetRecentlyViewedProducts
import com.example.shoppinggroceryapp.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val mGetRecentlyViewedProducts: GetRecentlyViewedProducts,
                    private val mGetProductsById: GetProductsById
):ViewModel() {

    var recentlyViewedList:MutableLiveData<MutableList<Product>> = MutableLiveData()
    fun getRecentlyViewedItems(){
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<Product>()
            val recentlyViewedProduct =mGetRecentlyViewedProducts.invoke(MainActivity.userId.toInt())
            if (recentlyViewedProduct != null) {
                for(i in recentlyViewedProduct){
                    val product: Product? = mGetProductsById.invoke(i.toLong())
                    product?.let {
                        list.add(it)
                    }
                }
            }
            list.reverse()
            var size = list.size
            if(size>20){
                recentlyViewedList.postValue(list.subList(0,20))
            }
            else{
                recentlyViewedList.postValue(list.subList(0,size))
            }
        }
//        Thread{
//            val list = mutableListOf<Product>()
//            val recentlyViewedProduct =mGetRecentlyViewedProducts.invoke(MainActivity.userId.toInt())
//            if (recentlyViewedProduct != null) {
//                for(i in recentlyViewedProduct){
//                    val product: Product? = mGetProductsById.invoke(i.toLong())
//                    product?.let {
//                        list.add(it)
//                    }
//                }
//            }
//            list.reverse()
//            var size = list.size
//            if(size>20){
//                recentlyViewedList.postValue(list.subList(0,20))
//            }
//            else{
//                recentlyViewedList.postValue(list.subList(0,size))
//            }
//        }.start()
    }
}