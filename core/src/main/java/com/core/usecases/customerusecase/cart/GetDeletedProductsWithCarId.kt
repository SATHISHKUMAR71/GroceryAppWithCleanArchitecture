package com.core.usecases.customerusecase.cart

import com.core.data.repository.CartRepository
import com.core.domain.products.CartWithProductData

class GetDeletedProductsWithCarId(private val cartRepository: CartRepository) {
    fun invoke(cartId:Int):List<CartWithProductData>{
        return cartRepository.getDeletedProductsWithCartId(cartId)!!
    }
}