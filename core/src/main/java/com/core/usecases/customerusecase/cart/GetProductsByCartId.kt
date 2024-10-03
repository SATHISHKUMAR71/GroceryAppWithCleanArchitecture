package com.core.usecases.customerusecase.cart

import com.core.data.repository.CartRepository
import com.core.domain.order.Cart
import com.core.domain.products.Product

class GetProductsByCartId(private val cartRepository: CartRepository) {
    fun invoke(cartId:Int):List<Product>{
        return cartRepository.getProductsByCartId(cartId)!!
    }
}