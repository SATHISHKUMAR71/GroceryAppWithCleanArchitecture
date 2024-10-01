package com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao

class OrderSummaryViewModelFactory(var retailerDao: RetailerDao):ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrderSummaryViewModel(retailerDao) as T
    }
}