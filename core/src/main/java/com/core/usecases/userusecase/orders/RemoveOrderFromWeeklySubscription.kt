package com.core.usecases.userusecase.orders

import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.order.WeeklyOnce

class RemoveOrderFromWeeklySubscription(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(weeklyOnce: WeeklyOnce){
        subscriptionRepository.deleteFromWeeklySubscription(weeklyOnce)
    }
}