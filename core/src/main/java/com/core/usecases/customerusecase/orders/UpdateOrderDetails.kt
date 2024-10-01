package com.core.usecases.customerusecase.orders

import com.core.data.repository.CustomerRepository
import com.core.data.repository.RetailerRepository
import com.core.domain.order.OrderDetails

class UpdateOrderDetails(private var customerRepository: CustomerRepository){
    fun invoke(orderDetails: OrderDetails){
        customerRepository.updateOrderDetails(orderDetails)
    }
}