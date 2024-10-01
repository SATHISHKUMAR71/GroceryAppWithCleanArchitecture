package com.core.usecases.userusecase.orders

import com.core.data.repository.UserRepository
import com.core.domain.order.WeeklyOnce

class RemoveOrderFromWeeklySubscription(private val userRepository: UserRepository) {
    fun invoke(weeklyOnce: WeeklyOnce){
        userRepository.deleteFromWeeklySubscription(weeklyOnce)
    }
}