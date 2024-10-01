package com.core.usecases.userusecase.orders

import com.core.data.repository.UserRepository
import com.core.domain.order.DailySubscription

class GetSpecificDailyOrderWithOrderId(private val userRepository: UserRepository) {
    fun invoke(orderId:Int): DailySubscription {
        return userRepository.getOrderForDailySubscription(orderId)
    }
}