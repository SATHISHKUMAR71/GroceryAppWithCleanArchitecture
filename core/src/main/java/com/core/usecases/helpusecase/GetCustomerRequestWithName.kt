package com.core.usecases.helpusecase

import com.core.data.repository.HelpRepository
import com.core.domain.help.CustomerRequestWithName

class GetCustomerRequestWithName(private val helpRepository: HelpRepository) {
    fun invoke():List<CustomerRequestWithName>?{
        return helpRepository.getDataFromCustomerReqWithName()
    }
}