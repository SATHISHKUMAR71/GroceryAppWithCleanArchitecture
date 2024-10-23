package com.example.shoppinggroceryapp.views.userviews.addressview.getaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.user.Address
import com.core.usecases.addressusecase.AddNewAddress
import com.core.usecases.addressusecase.UpdateAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetAddressViewModel(private val mAddNewAddress: AddNewAddress,
                          private val mUpdateAddress: UpdateAddress
):ViewModel() {


    fun addAddress(address: Address){
        viewModelScope.launch(Dispatchers.IO) {
            mAddNewAddress.invoke(address)
        }
    }

    fun updateAddress(address: Address){
        viewModelScope.launch(Dispatchers.IO) {
            mUpdateAddress.invoke(address)
        }
    }
}