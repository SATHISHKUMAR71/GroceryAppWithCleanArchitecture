package com.core.usecases.customerusecase.orders

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce

class AddDailySubscription(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(dailySubscription: DailySubscription){
        subscriptionRepository.addDailySubscription(dailySubscription)
    }
}