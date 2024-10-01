package com.core.usecases.userusecase

import com.core.data.repository.CustomerRepository
import com.core.domain.user.User

class UpdateExistingUser (private val customerRepository: CustomerRepository){
    fun invoke(user: User){
        customerRepository.updateExistingUser(user)
    }
}