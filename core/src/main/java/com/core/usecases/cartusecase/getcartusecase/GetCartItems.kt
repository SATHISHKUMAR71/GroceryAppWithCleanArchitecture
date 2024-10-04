package com.core.usecases.cartusecase.getcartusecase

import com.core.data.repository.CartRepository
import com.core.domain.order.Cart

class GetCartItems(private val cartRepository: CartRepository) {
    fun invoke(cartId:Int):List<Cart>{
        return cartRepository.getCartItems(cartId)?: listOf()
    }
}