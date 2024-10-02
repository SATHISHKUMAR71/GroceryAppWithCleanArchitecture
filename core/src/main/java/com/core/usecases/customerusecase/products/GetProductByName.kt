package com.core.usecases.customerusecase.products

import com.core.data.repository.CustomerRepository
import com.core.domain.products.Product

class GetProductByName(private var customerRepository: CustomerRepository) {
    fun invoke(query:String): List<Product>? {
        return customerRepository.getProductsByName(query)
    }
}