package com.core.usecases.customerusecase.products

import com.core.data.repository.CustomerRepository
import com.core.domain.order.OrderDetails

class GetBoughtProductList(private val customerRepository: CustomerRepository){
    fun invoke(userId:Int):List<OrderDetails>?{
        return customerRepository.getBoughtProductsList(userId)
    }
}