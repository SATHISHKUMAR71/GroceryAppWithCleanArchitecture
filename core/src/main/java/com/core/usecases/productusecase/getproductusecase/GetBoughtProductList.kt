package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.OrderRepository
import com.core.domain.order.OrderDetails

class GetBoughtProductList(private val orderRepository: OrderRepository){
    fun invoke(userId:Int):List<OrderDetails>?{
        return orderRepository.getBoughtProductsList(userId)
    }
}