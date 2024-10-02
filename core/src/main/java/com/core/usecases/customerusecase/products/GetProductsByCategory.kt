package com.core.usecases.customerusecase.products

import com.core.data.repository.CustomerRepository
import com.core.domain.products.Product

class GetProductsByCategory(private val customerRepository: CustomerRepository) {
    fun invoke(query:String): List<Product>? {
        return customerRepository.getProductByCategory(query)
    }
}