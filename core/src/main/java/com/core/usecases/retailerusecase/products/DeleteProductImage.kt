package com.core.usecases.retailerusecase.products

import com.core.data.repository.ProductRepository
import com.core.domain.products.Images

class DeleteProductImage(private val productRepository: ProductRepository) {
    fun invoke(images: Images){
        productRepository.deleteProductImage(images)
    }
}