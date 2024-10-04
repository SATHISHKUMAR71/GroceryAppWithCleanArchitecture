package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository

class GetRecentlyViewedProducts(private val productRepository: ProductRepository) {
    fun invoke(userId:Int):List<Int>?{
        return productRepository.getRecentlyViewedProducts(userId)
    }
}