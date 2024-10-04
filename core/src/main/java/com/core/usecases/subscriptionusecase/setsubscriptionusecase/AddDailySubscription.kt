package com.core.usecases.subscriptionusecase.setsubscriptionusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.DailySubscription

class AddDailySubscription(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(dailySubscription: DailySubscription){
        subscriptionRepository.addDailySubscription(dailySubscription)
    }
}