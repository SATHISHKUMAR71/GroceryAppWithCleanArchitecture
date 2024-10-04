package com.core.usecases.cartusecase.getcartusecase

import com.core.data.repository.CartRepository
import com.core.domain.products.CartWithProductData

class GetProductsWithCartData(private val cartRepository: CartRepository){
    fun invoke(cartId:Int):List<CartWithProductData>{
        return cartRepository.getProductsWithCartData(cartId)!!
    }
}