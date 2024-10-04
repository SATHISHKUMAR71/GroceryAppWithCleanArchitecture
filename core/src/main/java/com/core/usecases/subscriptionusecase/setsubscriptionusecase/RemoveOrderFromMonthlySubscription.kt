package com.core.usecases.subscriptionusecase.setsubscriptionusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.MonthlyOnce

class RemoveOrderFromMonthlySubscription(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(monthlyOnce: MonthlyOnce){
        subscriptionRepository.deleteFromMonthlySubscription(monthlyOnce)
    }
}