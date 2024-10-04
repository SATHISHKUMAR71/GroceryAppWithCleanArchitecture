package com.core.usecases.subscriptionusecase.setsubscriptionusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.DailySubscription

class RemoveOrderFromDailySubscription(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(dailySubscription: DailySubscription){
        subscriptionRepository.deleteFromDailySubscription(dailySubscription)
    }
}