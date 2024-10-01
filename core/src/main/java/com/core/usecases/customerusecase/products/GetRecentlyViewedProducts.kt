package com.core.usecases.customerusecase.products

import com.core.data.repository.CustomerRepository

class GetRecentlyViewedProducts(private val customerRepository: CustomerRepository) {
    fun invoke(userId:Int):List<Int>{
        return customerRepository.getRecentlyViewedProducts(userId)
    }
}