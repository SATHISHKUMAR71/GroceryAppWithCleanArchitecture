package com.example.shoppinggroceryapp.framework.data.authentication

import com.core.data.datasource.authenticationdatasource.AuthenticationDataSource
import com.core.domain.user.User
import com.example.shoppinggroceryapp.framework.data.ConvertorHelper
import com.example.shoppinggroceryapp.framework.db.dao.UserDao

class AuthenticationDataSourceImpl(private val userDao: UserDao):AuthenticationDataSource,
    ConvertorHelper() {
    override fun getUser(emailOrPhone: String, password: String): User? {
        return userDao.getUser(emailOrPhone,password)?.let {
            convertUserEntityToUser(it)
        }
    }

    override fun getUser(emailOrPhone: String): User? {

        return userDao.getUserData(emailOrPhone)?.let {
            convertUserEntityToUser(it)
        }

    }
}