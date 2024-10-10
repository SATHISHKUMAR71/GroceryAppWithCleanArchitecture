package com.core.usecases.orderusecase.getordersusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.DailySubscription
import com.core.domain.order.TimeSlot

class GetDailySubscriptionOrderWithTimeSlot(private var subscriptionRepository: SubscriptionRepository){
    fun invoke(orderId:Int): Map<DailySubscription, TimeSlot> {
        return subscriptionRepository.getDailySubscriptionWithTimeslot(orderId)
    }
}