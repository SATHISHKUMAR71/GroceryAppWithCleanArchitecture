package com.core.usecases.userusecase

import com.core.data.repository.ProductRepository
import com.core.data.repository.UserRepository

class GetChildNames(private val productRepository: ProductRepository) {
    fun invoke(parentName:String): List<String>? {
        return productRepository.getChildName(parentName)
    }
}