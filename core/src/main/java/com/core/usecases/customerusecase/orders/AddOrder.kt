package com.core.usecases.customerusecase.orders

import com.core.data.repository.CustomerRepository
import com.core.domain.order.OrderDetails

class AddOrder(private val customerRepository: CustomerRepository) {
    fun invoke(order:OrderDetails):Long{
        return customerRepository.addOrder(order)
    }
}