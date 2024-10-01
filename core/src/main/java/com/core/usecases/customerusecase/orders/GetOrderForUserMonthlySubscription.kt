package com.core.usecases.customerusecase.orders

import com.core.data.repository.CustomerRepository
import com.core.domain.order.OrderDetails

class GetOrderForUserMonthlySubscription(private val customerRepository: CustomerRepository) {
    fun invoke(userId:Int):List<OrderDetails>{
        return customerRepository.getOrdersForUserMonthlySubscription(userId)
    }
}