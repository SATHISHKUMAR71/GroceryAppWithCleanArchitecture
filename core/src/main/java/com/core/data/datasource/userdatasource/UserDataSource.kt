package com.core.data.datasource.userdatasource

import com.core.domain.user.User

interface UserDataSource {
    fun addNewUser(user: User)
    fun updateUser(user: User)
}