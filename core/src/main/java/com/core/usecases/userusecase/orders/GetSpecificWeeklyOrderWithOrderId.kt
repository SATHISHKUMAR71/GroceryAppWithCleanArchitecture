package com.core.usecases.userusecase.orders

import com.core.data.repository.UserRepository
import com.core.domain.order.WeeklyOnce

class GetSpecificWeeklyOrderWithOrderId(val userRepository: UserRepository){
    fun invoke(orderId:Int): WeeklyOnce? {
        return userRepository.getOrderedDayForWeekSubscription(orderId)
    }
}