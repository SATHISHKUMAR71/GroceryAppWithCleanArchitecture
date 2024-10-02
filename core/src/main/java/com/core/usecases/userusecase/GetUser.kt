package com.core.usecases.userusecase

import com.core.data.repository.CustomerRepository
import com.core.domain.user.User

class GetUser(private val customerRepository: CustomerRepository) {
    fun invoke(emailOrPhone:String,password:String): User?{
        return customerRepository.getUser(emailOrPhone,password)
    }
}