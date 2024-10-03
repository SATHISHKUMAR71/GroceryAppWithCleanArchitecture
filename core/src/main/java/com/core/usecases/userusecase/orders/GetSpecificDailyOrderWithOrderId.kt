package com.core.usecases.userusecase.orders

import com.core.data.repository.OrderRepository
import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.order.DailySubscription

class GetSpecificDailyOrderWithOrderId(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(orderId:Int): DailySubscription? {
        return subscriptionRepository.getOrderForDailySubscription(orderId)
    }
}