package com.core.usecases.orderusecase.getordersusecase

import com.core.data.repository.OrderRepository
import com.core.domain.order.OrderDetails

class GetOrderDetailsWithCartId (private val orderRepository: OrderRepository) {
    fun invoke(cartId:Int):OrderDetails{
        return orderRepository.getOrder(cartId)!!
    }
}