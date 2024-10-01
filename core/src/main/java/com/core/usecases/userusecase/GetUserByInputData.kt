package com.core.usecases.userusecase

import com.core.data.repository.CustomerRepository
import com.core.domain.user.User

class GetUserByInputData (private val customerRepository: CustomerRepository){
    fun invoke(emailOrPhone:String): User {
        return customerRepository.getUserData(emailOrPhone)
    }
}