package com.core.usecases.customerusecase.cart

import com.core.data.repository.CartRepository
import com.core.domain.order.Cart

class GetSpecificProductInCart (private val cartRepository: CartRepository){
    fun invoke(cartId:Int,productId:Int):Cart?{
        return cartRepository.getSpecificCart(cartId,productId)
    }
}