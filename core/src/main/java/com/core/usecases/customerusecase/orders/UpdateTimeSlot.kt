package com.core.usecases.customerusecase.orders

import com.core.data.repository.UserRepository
import com.core.domain.order.TimeSlot

class UpdateTimeSlot(private val userRepository: UserRepository) {
    fun invoke(timeSlot: TimeSlot){
        userRepository.updateTimeSlot(timeSlot)
    }
}