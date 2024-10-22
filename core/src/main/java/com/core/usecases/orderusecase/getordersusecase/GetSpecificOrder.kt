package com.core.usecases.orderusecase.getordersusecase

import com.core.data.repository.ProductRepository
import com.core.domain.order.OrderDetails

class GetSpecificOrder(private var productRepository: ProductRepository) {
    fun invoke(orderId: Int): OrderDetails {
        return productRepository.getSpecificOrder(orderId)
    }
}