package com.core.usecases.customerusecase.cart

import com.core.data.repository.CustomerRepository
import com.core.domain.order.Cart

class GetCartItems(private val customerRepository: CustomerRepository) {
    fun invoke(cartId:Int):List<Cart>{
        return customerRepository.getCartItems(cartId)?: listOf()
    }
}