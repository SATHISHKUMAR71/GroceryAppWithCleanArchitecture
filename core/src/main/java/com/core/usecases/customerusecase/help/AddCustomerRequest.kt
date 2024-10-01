package com.core.usecases.customerusecase.help

import com.core.data.repository.CustomerRepository
import com.core.domain.help.CustomerRequest

class AddCustomerRequest(private val customerRepository: CustomerRepository) {
    fun invoke(customerRequest: CustomerRequest){
        customerRepository.addCustomerRequest(customerRequest)
    }
}