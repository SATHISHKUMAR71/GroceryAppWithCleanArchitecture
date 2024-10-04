package com.core.usecases.orderusecase.getordersusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.DailySubscription

class GetSpecificDailyOrderWithOrderId(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(orderId:Int): DailySubscription? {
        return subscriptionRepository.getOrderForDailySubscription(orderId)
    }
}