package com.core.data.datasource.customerdatasource

import com.core.domain.user.Address

interface AddressDataSource {
    fun getAddress(addressId:Int):Address?
    fun addAddress(address: Address)
    fun updateAddress(address: Address)
    fun getAddressListForUser(userId:Int):List<Address>?
}