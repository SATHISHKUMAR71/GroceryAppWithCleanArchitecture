package com.core.usecases.userusecase.orders

import com.core.data.repository.UserRepository
import com.core.domain.order.DailySubscription

class RemoveOrderFromDailySubscription(private val userRepository: UserRepository) {
    fun invoke(dailySubscription: DailySubscription){
        userRepository.deleteFromDailySubscription(dailySubscription)
    }
}