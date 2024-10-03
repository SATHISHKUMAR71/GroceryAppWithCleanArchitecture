package com.core.usecases.userusecase.orders

import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.order.MonthlyOnce

class GetSpecificMonthlyOrderWithOrderId(private val subscriptionRepository: SubscriptionRepository){
    fun invoke(orderId:Int): MonthlyOnce? {
        return subscriptionRepository.getOrderedDayForMonthlySubscription(orderId)
    }
}