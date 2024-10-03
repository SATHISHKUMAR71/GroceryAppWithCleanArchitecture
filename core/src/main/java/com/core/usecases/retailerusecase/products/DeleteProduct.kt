package com.core.usecases.retailerusecase.products

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class DeleteProduct(private val productRepository: ProductRepository) {
    fun invoke(product: Product){
        productRepository.deleteProduct(product)
    }
}