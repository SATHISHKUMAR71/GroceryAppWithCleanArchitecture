package com.core.usecases.customerusecase.cart

import com.core.data.repository.CustomerRepository
import com.core.domain.order.CartMapping

class GetCartForUser(private val customerRepository: CustomerRepository) {
    fun invoke(userId:Int):CartMapping{
        return customerRepository.getCartForUser(userId)
    }
}