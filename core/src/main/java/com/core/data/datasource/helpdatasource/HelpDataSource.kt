package com.core.data.datasource.helpdatasource

import com.core.domain.help.CustomerRequest

interface HelpDataSource {
    fun addCustomerRequest(customerRequest: CustomerRequest)
}