package com.core.usecases.cartusecase.setcartusecase

import com.core.data.repository.CartRepository
import com.core.domain.order.CartMapping

class AddCartForUser(private val cartRepository: CartRepository){
    fun invoke(cartMapping: CartMapping){
        cartRepository.addCartForUser(cartMapping)
    }
}