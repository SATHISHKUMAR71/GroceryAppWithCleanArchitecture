package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class GetAllProducts(private val productRepository: ProductRepository) {
    fun invoke():List<Product>?{
        return productRepository.getOnlyProducts()
    }
}