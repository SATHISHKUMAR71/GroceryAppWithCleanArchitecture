package com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.user.User
import com.core.usecases.userusecase.AddNewUser
import com.core.usecases.userusecase.GetUserByInputData

class SignUpViewModel(private var mGetUserByInputData: GetUserByInputData,private var mAddNewUser: AddNewUser):ViewModel() {
    var registrationStatus:MutableLiveData<Boolean> = MutableLiveData()
    var registrationStatusInt:MutableLiveData<Int> = MutableLiveData()

    fun registerNewUser(user: User){
        Thread{

            val email = mGetUserByInputData.invoke(user.userEmail)
            val phone = mGetUserByInputData.invoke(user.userPhone)
            if(phone==null&&email==null) {
                Thread {
                    mAddNewUser.invoke(user)
                    var userEmail = user.userEmail
                    var userPhone = user.userPhone
                    registrationStatusInt.postValue(0)
                    registrationStatus.postValue(true)
                }.start()
            }
            else if(phone!=null && email!=null){
                registrationStatusInt.postValue(1)
            }
            else if(phone!=null){
                registrationStatusInt.postValue(2)
            }
            else{
                registrationStatusInt.postValue(3)
            }
        }.start()
    }
}