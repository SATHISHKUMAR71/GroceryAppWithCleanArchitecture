package com.core.usecases.helpusecase

import com.core.data.repository.HelpRepository
import com.core.domain.help.CustomerRequestWithName

class GetCustomerReqForSpecificUser (private var helpRepository: HelpRepository){
    fun invoke(userId:Int):List<CustomerRequestWithName>?{
        return helpRepository.getDataFromCustomerReqWithNameForSpecificUser(userId)
    }
}