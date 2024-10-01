package com.core.usecases.customerusecase.address

import com.core.data.repository.CustomerRepository
import com.core.domain.user.Address

class GetAllAddress (private val customerRepository: CustomerRepository){
    fun invoke(userId: Int): List<Address> {
        return customerRepository.getAddressListForUser(userId)
    }
}