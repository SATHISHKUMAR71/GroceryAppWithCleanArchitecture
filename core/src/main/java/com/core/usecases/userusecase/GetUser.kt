package com.core.usecases.userusecase

import com.core.data.repository.AuthenticationRepository
import com.core.domain.user.User

class GetUser(private val authenticationRepository: AuthenticationRepository) {
    fun invoke(emailOrPhone:String,password:String): User?{
        return authenticationRepository.getUser(emailOrPhone,password)
    }
}