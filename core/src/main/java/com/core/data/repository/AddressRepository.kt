package com.core.data.repository

import com.core.data.datasource.addressdatasource.AddressDataSource
import com.core.domain.user.Address

class AddressRepository(private val addressDataSource: AddressDataSource) {
    fun addNewAddress(address: Address) {
        addressDataSource.addAddress(address)
    }

    fun updateAddress(address: Address) {
        addressDataSource.updateAddress(address)
    }

    fun getAddressListForUser(userId: Int): List<Address>? {
        return addressDataSource.getAddressListForUser(userId)
    }

    fun getAddress(addressId:Int):Address?{
        return addressDataSource.getAddress(addressId)
    }
}