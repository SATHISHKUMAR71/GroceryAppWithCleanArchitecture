package com.core.usecases.customerusecase.orders

import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.domain.order.OrderDetails

class GetPurchasedProducts(private val orderRepository: OrderRepository) {
    fun invoke(userId:Int): List<OrderDetails>? {
        return orderRepository.getBoughtProductsList(userId)
    }
}