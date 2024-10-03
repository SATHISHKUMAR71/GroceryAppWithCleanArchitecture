package com.core.usecases.userusecase.orders

import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.order.DailySubscription

class RemoveOrderFromDailySubscription(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(dailySubscription: DailySubscription){
        subscriptionRepository.deleteFromDailySubscription(dailySubscription)
    }
}