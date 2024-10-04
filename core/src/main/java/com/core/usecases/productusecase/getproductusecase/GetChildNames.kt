package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository

class GetChildNames(private val productRepository: ProductRepository) {
    fun invoke(parentName:String): List<String>? {
        return productRepository.getChildName(parentName)
    }
}