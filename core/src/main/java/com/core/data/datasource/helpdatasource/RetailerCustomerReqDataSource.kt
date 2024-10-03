package com.core.data.datasource.helpdatasource

import com.core.domain.help.CustomerRequestWithName

interface RetailerCustomerReqDataSource {
    fun getDataFromCustomerReqWithName():List<CustomerRequestWithName>?
}