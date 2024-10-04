package com.example.shoppinggroceryapp.framework.data.user

import com.core.data.datasource.userdatasource.UserDataSource
import com.core.domain.user.User
import com.example.shoppinggroceryapp.framework.data.ConvertorHelper
import com.example.shoppinggroceryapp.framework.db.dao.UserDao

class UserDataSourceImpl(private val userDao: UserDao):UserDataSource{
    private var convertorHelper = ConvertorHelper()
    override fun addNewUser(user: User) {
        userDao.addUser(convertorHelper.convertUserToUserEntity(user))
    }

    override fun updateUser(user: User) {
        userDao.updateUser(convertorHelper.convertUserToUserEntity(user))
    }
}