package com.core.usecases.helpusecase

import com.core.data.repository.HelpRepository
import com.core.domain.help.CustomerRequest

class AddCustomerRequest(private val helpRepository: HelpRepository) {
    fun invoke(customerRequest: CustomerRequest){
        helpRepository.addCustomerRequest(customerRequest)
    }
}