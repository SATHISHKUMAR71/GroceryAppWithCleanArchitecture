package com.example.shoppinggroceryapp.views.userviews.addressview.getaddress

import androidx.lifecycle.ViewModel
import com.core.domain.user.Address
import com.core.usecases.customerusecase.address.AddNewAddress
import com.core.usecases.customerusecase.address.UpdateAddress
import com.example.shoppinggroceryapp.framework.db.dao.UserDao

class GetAddressViewModel(private val mAddNewAddress: AddNewAddress,
                          private val mUpdateAddress: UpdateAddress):ViewModel() {
    fun addAddress(address: Address){
        Thread{
            mAddNewAddress.invoke(address)
        }.start()
    }

    fun updateAddress(address: Address){
        Thread{
            mUpdateAddress.invoke(address)
        }.start()
    }
}