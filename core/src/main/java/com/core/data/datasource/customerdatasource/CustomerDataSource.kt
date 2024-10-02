package com.core.data.datasource.customerdatasource

import com.core.domain.help.CustomerRequest
import com.core.domain.order.OrderDetails
import com.core.domain.user.Address
import com.core.domain.user.User

interface CustomerDataSource: CartDataSource, OrderDataSource, ProductDataSource, AddressDataSource,AddSubscriptionDataSource {
    fun addNewUser(user: User)
    fun updateUser(user: User)
    fun updateAddress(address: Address)
    fun addCustomerRequest(customerRequest: CustomerRequest)
    fun getUser(emailOrPhone:String,password:String):User
    fun getUserData(emailOrPhone:String):User
    fun getOrderDetails(orderId:Int):OrderDetails
}