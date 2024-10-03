package com.core.usecases.customerusecase.address


import com.core.data.repository.AddressRepository
import com.core.domain.user.Address

class AddNewAddress(private var addressRepository: AddressRepository) {
    fun invoke(address: Address){
        addressRepository.addNewAddress(address)
    }
}