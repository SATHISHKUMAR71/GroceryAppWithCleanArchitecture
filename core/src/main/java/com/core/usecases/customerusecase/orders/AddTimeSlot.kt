package com.core.usecases.customerusecase.orders

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.TimeSlot

class AddTimeSlot(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(timeSlot: TimeSlot){
        subscriptionRepository.addTimeSlot(timeSlot)
    }
}