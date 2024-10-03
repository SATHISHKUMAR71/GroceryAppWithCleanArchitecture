package com.core.usecases.customerusecase.orders

import com.core.data.repository.OrderRepository
import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.OrderDetails

class GetOrderForUserMonthlySubscription(private val orderRepository: OrderRepository) {
    fun invoke(userId:Int):List<OrderDetails>?{
        return orderRepository.getOrdersForUserMonthlySubscription(userId)
    }
}