package com.example.shoppinggroceryapp.views.userviews.offer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinggroceryapp.framework.db.dao.UserDao

class OfferViewModelFactory(var userDao: UserDao):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OfferViewModel(userDao) as T
    }
}