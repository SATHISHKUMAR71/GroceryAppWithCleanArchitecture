package com.core.usecases.userusecase.orders

import com.core.data.repository.UserRepository
import com.core.domain.order.MonthlyOnce

class GetSpecificMonthlyOrderWithOrderId(private val userRepository: UserRepository){
    fun invoke(orderId:Int): MonthlyOnce? {
        return userRepository.getOrderedDayForMonthlySubscription(orderId)
    }
}