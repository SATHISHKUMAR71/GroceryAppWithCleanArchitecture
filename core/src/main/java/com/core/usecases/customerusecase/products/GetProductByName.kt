package com.core.usecases.customerusecase.products

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class GetProductByName(private var productRepository: ProductRepository) {
    fun invoke(query:String): List<Product>? {
        return productRepository.getProductsByName(query)
    }
}