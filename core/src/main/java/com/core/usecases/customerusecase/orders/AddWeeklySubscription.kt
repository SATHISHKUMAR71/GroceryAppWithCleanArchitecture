package com.core.usecases.customerusecase.orders

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.WeeklyOnce

class AddWeeklySubscription(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(weeklyOnce: WeeklyOnce){
        subscriptionRepository.addWeeklyOnceSubscription(weeklyOnce)
    }
}