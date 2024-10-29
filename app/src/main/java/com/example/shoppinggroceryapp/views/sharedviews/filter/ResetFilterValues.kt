package com.example.shoppinggroceryapp.views.sharedviews.filter

import androidx.lifecycle.MutableLiveData

object ResetFilterValues {
    fun resetFilterValues(){
        FilterFragmentSearch.isCheckBoxDiscountClicked = MutableLiveData()
        FilterFragmentSearch.isCheckBoxBrandClicked = MutableLiveData()
        FilterFragmentSearch.checkedList = mutableListOf()
        FilterFragmentSearch.checkedDiscountList = mutableListOf()
        FilterFragmentSearch.clearAll= MutableLiveData()
        FilterPrice.priceStartFrom = 0f
        FilterPrice.priceEndTo = FilterPrice.MAX_PRICE_VALUE
        FilterPrice.clearAll = MutableLiveData()
        FilterPrice.isPriceDataChanged = MutableLiveData()
        FilterExpiry.startManufactureDate = ""
        FilterExpiry.endManufactureDate = ""
        FilterExpiry.startExpiryDate = ""
        FilterExpiry.endExpiryDate= ""
        FilterExpiry.isExpiry = null
        FilterExpiry.clearAll = MutableLiveData()
        FilterExpiry.isDataChanged = MutableLiveData()
    }
}