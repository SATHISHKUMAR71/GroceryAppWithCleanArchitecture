package com.core.usecases.customerusecase.products

import com.core.data.repository.CustomerRepository

class GetBrandName(private val customerRepository: CustomerRepository) {
    fun invoke(id:Long):String?{
        return customerRepository.getBrandName(id)
    }
}