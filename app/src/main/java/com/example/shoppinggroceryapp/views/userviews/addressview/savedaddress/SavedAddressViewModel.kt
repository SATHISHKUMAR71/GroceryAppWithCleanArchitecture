package com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.user.Address
import com.core.usecases.customerusecase.address.GetAllAddress
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.user.AddressEntity

class SavedAddressViewModel(private val mGetAddressList: GetAllAddress):ViewModel() {

    var addressEntityList:MutableLiveData<List<Address>> = MutableLiveData()
    fun getAddressListForUser(userId:Int){
        Thread{
            addressEntityList.postValue(mGetAddressList.invoke(userId))
        }.start()
    }
}