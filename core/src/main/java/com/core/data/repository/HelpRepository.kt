package com.core.data.repository

import com.core.data.datasource.helpdatasource.HelpDataSource
import com.core.data.datasource.helpdatasource.RetailerCustomerReqDataSource
import com.core.domain.help.CustomerRequest
import com.core.domain.help.CustomerRequestWithName

class HelpRepository(private val customerHelpDataSource: HelpDataSource,private val retailerHelpDataSource: RetailerCustomerReqDataSource) {
    fun getDataFromCustomerReqWithName():List<CustomerRequestWithName>?{
        return retailerHelpDataSource.getDataFromCustomerReqWithName()
    }

    fun addCustomerRequest(customerRequest: CustomerRequest) {
        customerHelpDataSource.addCustomerRequest(customerRequest)
    }

    fun getDataFromCustomerReqWithNameForSpecificUser(userId:Int):List<CustomerRequestWithName>?{
        return customerHelpDataSource.getSpecificCustomerReqList(userId)
    }
}