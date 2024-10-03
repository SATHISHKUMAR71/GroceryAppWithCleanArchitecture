package com.core.usecases.userusecase.orders

import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.order.WeeklyOnce

class GetSpecificWeeklyOrderWithOrderId(val subscriptionRepository: SubscriptionRepository){
    fun invoke(orderId:Int): WeeklyOnce? {
        return subscriptionRepository.getOrderedDayForWeekSubscription(orderId)
    }
}