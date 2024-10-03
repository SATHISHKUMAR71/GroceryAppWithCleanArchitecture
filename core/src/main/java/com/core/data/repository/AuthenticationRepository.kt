package com.core.data.repository

import com.core.data.datasource.authenticationdatasource.AuthenticationDataSource
import com.core.domain.user.User

class AuthenticationRepository(private val authenticationDataSource: AuthenticationDataSource) {

    fun getUser(emailOrPhone:String,password:String): User?{
        return authenticationDataSource.getUser(emailOrPhone,password)
    }

    fun getUser(emailOrPhone: String):User?{
        return authenticationDataSource.getUser(emailOrPhone)
    }
}