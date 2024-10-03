package com.core.data.datasource.authenticationdatasource

import com.core.domain.user.User

interface AuthenticationDataSource {
    fun getUser(emailOrPhone:String,password:String): User?
    fun getUser(emailOrPhone:String): User?
}