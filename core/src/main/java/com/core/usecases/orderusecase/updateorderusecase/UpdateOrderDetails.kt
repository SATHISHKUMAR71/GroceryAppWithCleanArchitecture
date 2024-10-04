package com.core.usecases.orderusecase.updateorderusecase

import com.core.data.repository.OrderRepository
import com.core.domain.order.OrderDetails

class UpdateOrderDetails(private var orderRepository: OrderRepository){
    fun invoke(orderDetails: OrderDetails){
        orderRepository.updateOrderDetails(orderDetails)
    }
}