package com.core.usecases.customerusecase.cart

import com.core.data.repository.CustomerRepository
import com.core.domain.order.Cart

class AddProductInCart(private val customerRepository: CustomerRepository) {
    fun invoke(cart:Cart){
        customerRepository.addItemsToCart(cart)
    }
}