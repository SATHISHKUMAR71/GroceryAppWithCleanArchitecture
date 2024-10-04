package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository

class GetBrandName(private val productRepository: ProductRepository) {
    fun invoke(id:Long):String?{
        return productRepository.getBrandName(id)
    }
}