package com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.order.CartMapping
import com.core.domain.user.User
import com.core.usecases.cartusecase.setcartusecase.AddCartForUser
import com.core.usecases.cartusecase.getcartusecase.GetCartForUser
import com.core.usecases.userusecase.GetUser
import com.core.usecases.userusecase.GetUserByInputData
import com.example.shoppinggroceryapp.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private var mGetUser: GetUser, private var mGetUserByInputData: GetUserByInputData, private var mGetCartForUser: GetCartForUser,
                     private var addCartForUser: AddCartForUser
) :ViewModel(){
    var user:MutableLiveData<User> = MutableLiveData()
    var userName:MutableLiveData<User> = MutableLiveData()

    fun isUser(userData:String){
        viewModelScope.launch (Dispatchers.IO){
            userName.postValue(mGetUserByInputData.invoke(userData))
        }
//        Thread{
//            userName.postValue(mGetUserByInputData.invoke(userData))
//        }.start()
    }

    fun validateUser(email:String,password:String){
        viewModelScope.launch (Dispatchers.IO){
            user.postValue(mGetUser.invoke(email, password))
        }
//        Thread {
//            user.postValue(mGetUser.invoke(email, password))
//        }.start()
    }

    fun assignCartForUser(){
        viewModelScope.launch (Dispatchers.IO){
            val cart: CartMapping? = mGetCartForUser.invoke(user.value?.userId?:-1)
            if(cart==null){
                addCartForUser.invoke(CartMapping(0,user.value?.userId?:-1,"available"))
                val newCart: CartMapping? =  mGetCartForUser.invoke(user.value?.userId?:-1)
                while (newCart==null) {
                }
                MainActivity.cartId = newCart.cartId
            }
            else{
                MainActivity.cartId = cart.cartId
            }
        }
//        Thread{
//            val cart: CartMapping? = mGetCartForUser.invoke(user.value?.userId?:-1)
//            if(cart==null){
//                addCartForUser.invoke(CartMapping(0,user.value?.userId?:-1,"available"))
//                val newCart: CartMapping? =  mGetCartForUser.invoke(user.value?.userId?:-1)
//                while (newCart==null) {
//                }
//                MainActivity.cartId = newCart.cartId
//            }
//            else{
//                MainActivity.cartId = cart.cartId
//            }
//        }.start()
    }
}