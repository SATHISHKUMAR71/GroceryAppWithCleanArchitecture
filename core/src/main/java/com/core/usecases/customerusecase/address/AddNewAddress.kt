package com.core.usecases.customerusecase.address

import com.core.data.datasource.customerdatasource.CustomerDataSource
import com.core.data.repository.CustomerRepository
import com.core.domain.user.Address

class AddNewAddress(private var customerRepository: CustomerRepository) {
    fun invoke(address: Address){
        customerRepository.addNewAddress(address)
    }
}