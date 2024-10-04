package com.core.usecases.subscriptionusecase.getsubscriptionusecase

import com.core.data.repository.OrderRepository
import com.core.domain.order.OrderDetails

class GetOrderForUserWeeklySubscription(private val orderRepository: OrderRepository){
    fun invoke(userId:Int):List<OrderDetails>?{
        return orderRepository.getOrdersForUserWeeklySubscription(userId)
    }
}