package com.core.usecases.customerusecase.cart

import com.core.data.repository.CustomerRepository
import com.core.domain.order.Cart
import com.core.domain.products.Product

class GetProductsByCartId(private val customerRepository: CustomerRepository) {
    fun invoke(cartId:Int):List<Product>{
        return customerRepository.getProductsByCartId(cartId)!!
    }
}