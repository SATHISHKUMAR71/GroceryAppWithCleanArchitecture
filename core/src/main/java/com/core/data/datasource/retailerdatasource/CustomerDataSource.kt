package com.core.data.datasource.retailerdatasource

import com.core.domain.help.CustomerRequestWithName
import com.core.domain.order.OrderDetails

interface CustomerDataSource {
    fun getDataFromCustomerReqWithName():List<CustomerRequestWithName>?
}