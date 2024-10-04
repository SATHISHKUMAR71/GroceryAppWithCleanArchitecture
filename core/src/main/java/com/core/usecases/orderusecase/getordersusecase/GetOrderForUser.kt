package com.core.usecases.orderusecase.getordersusecase

import com.core.data.repository.OrderRepository
import com.core.domain.order.OrderDetails

class GetOrderForUser(private val orderRepository: OrderRepository) {
    fun invoke(userId:Int):List<OrderDetails>?{
        return orderRepository.getOrdersForUser(userId)
    }
}