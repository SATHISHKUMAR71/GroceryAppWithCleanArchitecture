package com.core.usecases.customerusecase.orders

import com.core.data.repository.CustomerRepository
import com.core.domain.order.MonthlyOnce

class AddMonthlySubscription(private val customerRepository: CustomerRepository) {
    fun invoke(monthlyOnce: MonthlyOnce){
        customerRepository.addMonthlyOnceSubscription(monthlyOnce)
    }
}