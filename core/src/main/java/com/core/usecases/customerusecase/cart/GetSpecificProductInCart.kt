package com.core.usecases.customerusecase.cart

import com.core.data.repository.CustomerRepository
import com.core.domain.order.Cart

class GetSpecificProductInCart (private val customerRepository: CustomerRepository){
    fun invoke(cartId:Int,productId:Int):Cart{
        return customerRepository.getSpecificCart(cartId,productId)
    }
}