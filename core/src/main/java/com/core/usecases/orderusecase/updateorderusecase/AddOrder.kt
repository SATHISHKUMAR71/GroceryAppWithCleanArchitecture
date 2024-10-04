package com.core.usecases.orderusecase.updateorderusecase

import com.core.data.repository.OrderRepository
import com.core.domain.order.OrderDetails

class AddOrder(private val orderRepository: OrderRepository) {
    fun invoke(order:OrderDetails):Long?{
        return orderRepository.addOrder(order)
    }
}