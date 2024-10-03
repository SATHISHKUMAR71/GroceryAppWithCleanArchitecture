package com.core.usecases.customerusecase.cart

import com.core.data.repository.CartRepository
import com.core.domain.order.Cart

class AddProductInCart(private val cartRepository: CartRepository) {
    fun invoke(cart:Cart){
        cartRepository.addItemsToCart(cart)
    }
}