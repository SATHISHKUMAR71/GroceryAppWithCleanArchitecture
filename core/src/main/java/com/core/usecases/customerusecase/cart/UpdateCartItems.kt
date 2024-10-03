package com.core.usecases.customerusecase.cart

import com.core.data.repository.CartRepository
import com.core.domain.order.Cart

class UpdateCartItems(private val cartRepository: CartRepository) {
    fun invoke(cart: Cart){
        cartRepository.updateCartItems(cart)
    }
}