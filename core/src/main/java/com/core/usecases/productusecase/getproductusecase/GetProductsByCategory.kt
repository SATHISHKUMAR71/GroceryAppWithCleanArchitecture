package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class GetProductsByCategory(private val productRepository: ProductRepository) {
    fun invoke(query:String): List<Product>? {
        return productRepository.getProductByCategory(query)
    }
}