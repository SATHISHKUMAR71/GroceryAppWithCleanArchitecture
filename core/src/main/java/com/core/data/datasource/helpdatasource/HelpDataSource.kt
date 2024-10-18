package com.core.data.datasource.helpdatasource

import com.core.domain.help.CustomerRequest
import com.core.domain.help.CustomerRequestWithName

interface HelpDataSource {
    fun addCustomerRequest(customerRequest: CustomerRequest)
    fun getSpecificCustomerReqList(userId:Int):List<CustomerRequestWithName>?
}