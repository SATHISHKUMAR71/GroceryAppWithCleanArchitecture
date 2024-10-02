package com.core.usecases.customerusecase.products

import com.core.data.repository.CustomerRepository
import com.core.domain.products.Product

class GetAllProducts(private val customerRepository: CustomerRepository) {
    fun invoke():List<Product>?{
        return customerRepository.getOnlyProducts()
    }
}