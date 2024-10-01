package com.core.usecases.customerusecase.cart

import com.core.data.repository.CustomerRepository
import com.core.domain.order.CartMapping

class UpdateCart(private val customerRepository: CustomerRepository){
    fun invoke(cartMapping: CartMapping){
        customerRepository.updateCartMapping(cartMapping)
    }
}