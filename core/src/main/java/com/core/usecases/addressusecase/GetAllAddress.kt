package com.core.usecases.addressusecase

import com.core.data.repository.AddressRepository
import com.core.domain.user.Address

class GetAllAddress (private val addressRepository: AddressRepository){
    fun invoke(userId: Int): List<Address> {
        return addressRepository.getAddressListForUser(userId)?: listOf()
    }
}