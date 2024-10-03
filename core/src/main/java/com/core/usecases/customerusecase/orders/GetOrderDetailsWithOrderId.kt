package com.core.usecases.customerusecase.orders

import com.core.data.repository.OrderRepository
import com.core.domain.order.OrderDetails

class GetOrderDetailsWithOrderId(private val orderRepository: OrderRepository){
    fun invoke(orderId:Int):OrderDetails?{
        return orderRepository.getOrderDetails(orderId)
    }
}