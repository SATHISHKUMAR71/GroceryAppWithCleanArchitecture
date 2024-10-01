package com.core.usecases.retailerusecase.customer

import com.core.data.repository.RetailerRepository
import com.core.domain.help.CustomerRequestWithName

class GetCustomerRequestWithName(private val retailerRepository: RetailerRepository) {
    fun invoke():List<CustomerRequestWithName>{
        return retailerRepository.getDataFromCustomerReqWithName()
    }
}