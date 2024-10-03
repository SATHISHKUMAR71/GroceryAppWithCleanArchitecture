package com.core.usecases.customerusecase.orders

import com.core.data.repository.OrderRepository
import com.core.domain.order.OrderDetails

class AddOrder(private val orderRepository: OrderRepository) {
    fun invoke(order:OrderDetails):Long?{
        return orderRepository.addOrder(order)
    }
}