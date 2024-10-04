package com.core.usecases.subscriptionusecase.setsubscriptionusecase

import com.core.data.repository.SubscriptionRepository
import com.core.domain.order.TimeSlot

class UpdateTimeSlot(private val subscriptionRepository: SubscriptionRepository) {
    fun invoke(timeSlot: TimeSlot){
        subscriptionRepository.updateTimeSlot(timeSlot)
    }
}