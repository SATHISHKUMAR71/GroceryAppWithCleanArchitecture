package com.core.usecases.customerusecase.orders

import com.core.data.repository.CustomerRepository
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce

class AddDailySubscription(private val customerRepository: CustomerRepository) {
    fun invoke(dailySubscription: DailySubscription){
        customerRepository.addDailySubscription(dailySubscription)
    }
}