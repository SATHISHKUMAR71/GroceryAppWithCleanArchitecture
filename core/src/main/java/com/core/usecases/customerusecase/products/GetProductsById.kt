package com.core.usecases.customerusecase.products

import com.core.data.repository.CustomerRepository
import com.core.domain.products.Product

class GetProductsById(private val customerRepository: CustomerRepository) {
    fun invoke(productId:Long):Product?{
        return customerRepository.getProductsById(productId)
    }
}