package com.core.usecases.customerusecase.products

import com.core.data.repository.CustomerRepository

class GetProductsByQuery(private val customerRepository: CustomerRepository){
    fun invoke(query:String):List<String>{
        return customerRepository.getProductsForQuery(query)
    }
}