package com.core.usecases.customerusecase.products

import com.core.data.repository.ProductRepository
import com.core.domain.products.Images

class GetImagesForProduct(private val productRepository: ProductRepository) {
    fun invoke(productId:Long):List<Images>?{
        return productRepository.getImagesForProduct(productId)
    }
}