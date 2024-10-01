package com.core.usecases.customerusecase.cart

import com.core.data.repository.CustomerRepository
import com.core.domain.order.CartMapping

class AddCartForUser(private val customerRepository: CustomerRepository){
    fun invoke(cartMapping: CartMapping){
        customerRepository.addCartForUser(cartMapping)
    }
}