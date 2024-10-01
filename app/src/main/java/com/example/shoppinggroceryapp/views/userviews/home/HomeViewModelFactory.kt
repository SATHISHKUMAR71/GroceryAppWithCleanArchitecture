package com.example.shoppinggroceryapp.views.userviews.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinggroceryapp.framework.db.dao.ProductDao

class HomeViewModelFactory(var productDao: ProductDao):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(productDao) as T
    }
}