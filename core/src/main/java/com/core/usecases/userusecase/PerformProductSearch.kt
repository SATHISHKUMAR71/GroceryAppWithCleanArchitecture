package com.core.usecases.userusecase

import com.core.data.repository.UserRepository

class PerformProductSearch(private val userRepository: UserRepository) {
    operator fun invoke(query:String):List<String>?{
        return userRepository.getProductForQueryName(query)
    }
}