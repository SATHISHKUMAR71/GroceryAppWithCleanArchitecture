package com.core.usecases.addressusecase

import com.core.data.repository.AddressRepository
import com.core.domain.user.Address

class UpdateAddress(private val addressRepository: AddressRepository) {
    fun invoke(address: Address){
        addressRepository.updateAddress(address)
    }
}