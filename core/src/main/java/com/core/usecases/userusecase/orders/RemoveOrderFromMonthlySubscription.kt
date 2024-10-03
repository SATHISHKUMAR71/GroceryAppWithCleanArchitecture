package com.core.usecases.userusecase.orders

import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.order.MonthlyOnce

class RemoveOrderFromMonthlySubscription(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(monthlyOnce: MonthlyOnce){
        subscriptionRepository.deleteFromMonthlySubscription(monthlyOnce)
    }
}