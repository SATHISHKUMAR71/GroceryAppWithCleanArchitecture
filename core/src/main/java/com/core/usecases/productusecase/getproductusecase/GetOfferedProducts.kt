package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class GetOfferedProducts(private val productRepository: ProductRepository) {
    fun invoke():List<Product>?{
        return productRepository.getOfferedProducts()
    }
}