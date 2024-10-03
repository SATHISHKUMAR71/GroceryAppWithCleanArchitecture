package com.example.shoppinggroceryapp.framework.data.address

import com.core.data.datasource.addressdatasource.AddressDataSource
import com.core.domain.order.TimeSlot
import com.core.domain.user.Address
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.order.TimeSlotEntity
import com.example.shoppinggroceryapp.framework.db.entity.user.AddressEntity

class AddressDataSourceImpl(private val userDao: UserDao):AddressDataSource {
    override fun getAddress(addressId: Int): Address? {
        return userDao.getAddress(addressId)?.let {address ->
            Address(address.addressId,address.userId,address.addressContactName,address.addressContactNumber,address.buildingName
                ,address.streetName,address.city,address.state,address.country,address.postalCode)
        }
    }

    override fun addAddress(address: Address) {
        userDao.addAddress(
            AddressEntity(address.addressId,address.userId,address.addressContactName,address.addressContactNumber,address.buildingName
            ,address.streetName,address.city,address.state,address.country,address.postalCode)
        )
    }

    override fun getAddressListForUser(userId: Int): List<Address>? {
        return userDao.getAddressListForUser(userId)?.map { address -> Address(address.addressId,address.userId,address.addressContactName,address.addressContactNumber,address.buildingName
            ,address.streetName,address.city,address.state,address.country,address.postalCode) }
    }

    override fun updateAddress(address: Address) {
        userDao.updateAddress(
            AddressEntity(address.addressId,address.userId,address.addressContactName,
                address.addressContactNumber,address.buildingName,address.streetName,address.city,address.state,address.country,address.postalCode)
        )
    }
}