package com.core.usecases.customerusecase.address

import com.core.data.repository.CustomerRepository
import com.core.domain.user.Address

class GetSpecificAddress(private val customerRepository: CustomerRepository) {
    fun invoke(addressId:Int):Address{
        return customerRepository.getAddress(addressId)!!
    }
}