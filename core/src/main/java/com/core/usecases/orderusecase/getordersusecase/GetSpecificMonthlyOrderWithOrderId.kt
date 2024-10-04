package com.core.usecases.orderusecase.getordersusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.MonthlyOnce

class GetSpecificMonthlyOrderWithOrderId(private val subscriptionRepository: SubscriptionRepository){
    fun invoke(orderId:Int): MonthlyOnce? {
        return subscriptionRepository.getOrderedDayForMonthlySubscription(orderId)
    }
}