package com.core.usecases.customerusecase.cart

import com.core.data.repository.CartRepository
import com.core.domain.order.CartMapping

class GetCartForUser(private val cartRepository: CartRepository) {
    fun invoke(userId:Int):CartMapping?{
        return cartRepository.getCartForUser(userId)
    }
}