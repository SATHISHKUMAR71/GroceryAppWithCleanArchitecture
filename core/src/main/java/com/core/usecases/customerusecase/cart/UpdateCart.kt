package com.core.usecases.customerusecase.cart

import com.core.data.repository.CartRepository
import com.core.domain.order.CartMapping

class UpdateCart(private val cartRepository: CartRepository){
    fun invoke(cartMapping: CartMapping){
        cartRepository.updateCartMapping(cartMapping)
    }
}