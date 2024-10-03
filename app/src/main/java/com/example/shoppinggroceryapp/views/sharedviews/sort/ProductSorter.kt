package com.example.shoppinggroceryapp.views.sharedviews.sort

import com.core.domain.products.Product
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity


class ProductSorter {

    fun sortByDate(productEntityList:List<Product>):List<Product>{
        val sortedList =productEntityList.sortedWith(compareBy({it.manufactureDate},{it.productId}))
        return sortedList
    }
    fun sortByExpiryDate(productEntityList:List<Product>):List<Product>{
        val sortedList =productEntityList.sortedWith(compareBy({it.expiryDate},{it.productId}))
        return sortedList
    }
    fun sortByDiscount(productEntityList:List<Product>):List<Product>{
        val sortedList =productEntityList.sortedWith(compareBy({it.offer},{it.productId})).reversed()
        return sortedList
    }
    fun sortByPriceHighToLow(productEntityList:List<Product>):List<Product>{

        val sortedList =productEntityList.sortedWith(compareBy({(it.price)-(it.price * it.offer/100)},{it.productId})).reversed()
        return sortedList
    }

    fun sortByPriceLowToHigh(productEntityList:List<Product>):List<Product>{
        val sortedList =productEntityList.sortedWith(compareBy({(it.price)-(it.price * it.offer/100)},{it.productId}))
        return sortedList
    }
}