package com.core.usecases.orderusecase.getordersusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce

class GetSpecificWeeklyOrderWithTimeSlot (private var subscriptionRepository: SubscriptionRepository){
    fun invoke(orderId:Int): Map<WeeklyOnce, TimeSlot> {
        return subscriptionRepository.getWeeklySubscriptionWithTimeslot(orderId)
    }
}