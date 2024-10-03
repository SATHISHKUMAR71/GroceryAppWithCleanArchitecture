package com.core.usecases.customerusecase.products

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class GetProductsById(private val productRepository: ProductRepository) {
    fun invoke(productId:Long):Product?{
        return productRepository.getProductsById(productId)
    }
}