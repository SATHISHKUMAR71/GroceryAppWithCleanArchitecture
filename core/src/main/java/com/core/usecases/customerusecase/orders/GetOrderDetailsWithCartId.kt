package com.core.usecases.customerusecase.orders

import com.core.data.repository.CustomerRepository
import com.core.domain.order.OrderDetails

class GetOrderDetailsWithCartId (private val customerRepository: CustomerRepository) {
    fun invoke(cartId:Int):OrderDetails{
        return customerRepository.getOrder(cartId)
    }
}