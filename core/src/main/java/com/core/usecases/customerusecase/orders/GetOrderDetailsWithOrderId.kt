package com.core.usecases.customerusecase.orders

import com.core.data.repository.CustomerRepository
import com.core.domain.order.OrderDetails

class GetOrderDetailsWithOrderId(private val customerRepository: CustomerRepository){
    fun invoke(orderId:Int):OrderDetails?{
        return customerRepository.getOrderDetails(orderId)
    }
}