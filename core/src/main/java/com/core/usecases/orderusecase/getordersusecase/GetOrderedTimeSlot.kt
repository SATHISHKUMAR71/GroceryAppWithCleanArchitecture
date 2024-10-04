package com.core.usecases.orderusecase.getordersusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.TimeSlot

class GetOrderedTimeSlot(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(orderId:Int): TimeSlot? {
        return subscriptionRepository.getOrderedTimeSlot(orderId)
    }
}