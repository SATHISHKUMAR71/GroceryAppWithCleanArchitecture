package com.core.usecases.customerusecase.orders

import com.core.data.repository.CustomerRepository
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.WeeklyOnce

class AddWeeklySubscription(private val customerRepository: CustomerRepository) {
    fun invoke(weeklyOnce: WeeklyOnce){
        customerRepository.addWeeklyOnceSubscription(weeklyOnce)
    }
}