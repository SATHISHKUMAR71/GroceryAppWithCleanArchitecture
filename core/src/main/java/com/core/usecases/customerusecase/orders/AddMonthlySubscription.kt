package com.core.usecases.customerusecase.orders

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.MonthlyOnce

class AddMonthlySubscription(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(monthlyOnce: MonthlyOnce){
        subscriptionRepository.addMonthlyOnceSubscription(monthlyOnce)
    }
}