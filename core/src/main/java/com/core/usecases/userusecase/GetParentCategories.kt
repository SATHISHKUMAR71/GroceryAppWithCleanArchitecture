package com.core.usecases.userusecase

import com.core.data.repository.UserRepository
import com.core.domain.products.ParentCategory

class GetParentCategories(private val userRepository: UserRepository) {
    fun invoke():List<ParentCategory>{
        return userRepository.getParentCategoryList()
    }
}