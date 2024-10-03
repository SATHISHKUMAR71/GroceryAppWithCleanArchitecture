package com.core.usecases.userusecase

import com.core.data.repository.AuthenticationRepository
import com.core.data.repository.UserRepository
import com.core.domain.user.User

class GetUserByInputData (private val authenticationRepository: AuthenticationRepository){
    fun invoke(emailOrPhone:String): User? {
        return authenticationRepository.getUser(emailOrPhone)
    }
}