package com.core.usecases.cartusecase.setcartusecase

import com.core.data.repository.CartRepository
import com.core.domain.order.Cart

class AddProductInCart(private val cartRepository: CartRepository) {
    fun invoke(cart:Cart){
        cartRepository.addItemsToCart(cart)
    }
}