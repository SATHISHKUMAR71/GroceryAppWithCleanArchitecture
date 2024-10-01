package com.core.usecases.customerusecase.address

import com.core.data.repository.CustomerRepository
import com.core.domain.user.Address

class UpdateAddress(private val customerRepository: CustomerRepository) {
    fun invoke(address: Address){
        customerRepository.updateAddress(address)
    }
}