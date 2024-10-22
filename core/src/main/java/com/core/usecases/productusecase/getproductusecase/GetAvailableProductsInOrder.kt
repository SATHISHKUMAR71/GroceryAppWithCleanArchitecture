package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.products.ProductAndDeletedCounts

class GetAvailableProductsInOrder(var productRepository: ProductRepository){
    fun invoke(orderId: Int): ProductAndDeletedCounts {
        return productRepository.getAvailableProductsInOrderId(orderId)
    }
}