package com.core.usecases.userusecase.orders

import com.core.data.repository.UserRepository
import com.core.domain.order.TimeSlot

class GetOrderedTimeSlot(private val userRepository: UserRepository) {
    fun invoke(orderId:Int): TimeSlot? {
        return userRepository.getOrderedTimeSlot(orderId)
    }
}