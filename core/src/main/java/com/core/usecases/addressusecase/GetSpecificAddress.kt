package com.core.usecases.addressusecase

import com.core.data.repository.AddressRepository
import com.core.domain.user.Address

class GetSpecificAddress(private val addressRepository: AddressRepository) {
    fun invoke(addressId:Int):Address{
        return addressRepository.getAddress(addressId)!!
    }
}