package com.core.usecases.customerusecase.orders

import com.core.data.repository.OrderRepository
import com.core.domain.order.OrderDetails

class GetOrdersForUserNoSubscription (private val orderRepository: OrderRepository) {
    fun invoke(userId:Int):List<OrderDetails>?{
        return orderRepository.getOrdersForUserNoSubscription(userId)
    }
}