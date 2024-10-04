package com.core.usecases.subscriptionusecase.setsubscriptionusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.WeeklyOnce

class RemoveOrderFromWeeklySubscription(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(weeklyOnce: WeeklyOnce){
        subscriptionRepository.deleteFromWeeklySubscription(weeklyOnce)
    }
}