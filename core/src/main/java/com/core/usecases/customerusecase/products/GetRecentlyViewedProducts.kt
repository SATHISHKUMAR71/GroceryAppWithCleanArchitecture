package com.core.usecases.customerusecase.products

import com.core.data.repository.ProductRepository

class GetRecentlyViewedProducts(private val productRepository: ProductRepository) {
    fun invoke(userId:Int):List<Int>?{
        return productRepository.getRecentlyViewedProducts(userId)
    }
}