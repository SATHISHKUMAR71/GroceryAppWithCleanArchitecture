package com.core.usecases.userusecase.orders

import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.order.TimeSlot

class GetOrderedTimeSlot(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(orderId:Int): TimeSlot? {
        return subscriptionRepository.getOrderedTimeSlot(orderId)
    }
}