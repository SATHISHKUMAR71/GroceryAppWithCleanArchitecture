package com.core.usecases.orderusecase.getordersusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.TimeSlot

class GetMonthlySubscriptionWithTimeSlot(var subscriptionRepository: SubscriptionRepository) {
    fun invoke(orderId:Int): Map<MonthlyOnce, TimeSlot> {
        return subscriptionRepository.getMonthlySubscriptionWithTimeslot(orderId)
    }
}