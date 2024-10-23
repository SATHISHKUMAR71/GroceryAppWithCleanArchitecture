package com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.user.Address
import com.core.usecases.addressusecase.GetAllAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedAddressViewModel(private val mGetAddressList: GetAllAddress):ViewModel() {

    var addressEntityList:MutableLiveData<List<Address>> = MutableLiveData()
    fun getAddressListForUser(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            addressEntityList.postValue(mGetAddressList.invoke(userId))
        }
//        Thread{
//            addressEntityList.postValue(mGetAddressList.invoke(userId))
//        }.start()
    }
}