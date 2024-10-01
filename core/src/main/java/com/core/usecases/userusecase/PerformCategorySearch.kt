package com.core.usecases.userusecase

import com.core.data.repository.UserRepository

class PerformCategorySearch(private var userRepository: UserRepository) {
    operator fun invoke(query:String):List<String>{
        return userRepository.getProductsForQuery(query)
    }
}