package com.core.usecases.userusecase.orders

import com.core.data.repository.UserRepository
import com.core.domain.order.MonthlyOnce

class RemoveOrderFromMonthlySubscription(private val userRepository: UserRepository) {
    fun invoke(monthlyOnce: MonthlyOnce){
        userRepository.deleteFromMonthlySubscription(monthlyOnce)
    }
}