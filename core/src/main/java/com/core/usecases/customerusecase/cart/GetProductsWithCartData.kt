package com.core.usecases.customerusecase.cart

import com.core.data.repository.CustomerRepository
import com.core.domain.products.CartWithProductData

class GetProductsWithCartData(private val customerRepository: CustomerRepository){
    fun invoke(cartId:Int):List<CartWithProductData>{
        return customerRepository.getProductsWithCartData(cartId)
    }
}