package com.example.shoppinggroceryapp.framework.data.help

import com.core.data.datasource.helpdatasource.HelpDataSource
import com.core.data.datasource.helpdatasource.RetailerCustomerReqDataSource
import com.core.domain.help.CustomerRequest
import com.core.domain.help.CustomerRequestWithName
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.help.CustomerRequestEntity

class HelpDataSourceImpl(private val retailerDao: RetailerDao):HelpDataSource,RetailerCustomerReqDataSource {

    override fun addCustomerRequest(customerRequest: CustomerRequest) {
        retailerDao.addCustomerRequest(
            CustomerRequestEntity(customerRequest.helpId,customerRequest.userId,customerRequest.requestedDate,
                customerRequest.orderId,customerRequest.request)
        )
    }

    override fun getSpecificCustomerReqList(userId: Int): List<CustomerRequestWithName>? {
        return retailerDao.getCustomerReqWithNameForSpecificCustomer(userId)?.map { CustomerRequestWithName(it.helpId,it.userId,it.requestedDate,it.orderId,it.request,it.userFirstName,it.userLastName,it.userEmail,it.userPhone) }
    }

    override fun getDataFromCustomerReqWithName(): List<CustomerRequestWithName>? {
        return retailerDao.getDataFromCustomerReqWithName()?.map { CustomerRequestWithName(it.helpId,it.userId,it.requestedDate,it.orderId,it.request,it.userFirstName,it.userLastName,it.userEmail,it.userPhone) }
    }
}