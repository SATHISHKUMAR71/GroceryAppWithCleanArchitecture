package com.core.usecases.customerusecase.products

import com.core.data.repository.CustomerRepository

class GetProductsQueryForName(private val customerRepository: CustomerRepository) {
    fun invoke(query:String):List<String>{
        return customerRepository.getProductForQueryName(query)
    }
}