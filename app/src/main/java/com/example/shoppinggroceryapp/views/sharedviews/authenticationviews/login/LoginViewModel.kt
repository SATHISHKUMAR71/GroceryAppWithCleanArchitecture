package com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.order.CartMapping
import com.core.domain.user.User
import com.core.usecases.cartusecase.setcartusecase.AddCartForUser
import com.core.usecases.cartusecase.getcartusecase.GetCartForUser
import com.core.usecases.userusecase.GetUser
import com.core.usecases.userusecase.GetUserByInputData
import com.example.shoppinggroceryapp.MainActivity

class LoginViewModel(private var mGetUser: GetUser, private var mGetUserByInputData: GetUserByInputData, private var mGetCartForUser: GetCartForUser,
                     private var addCartForUser: AddCartForUser
) :ViewModel(){
    var user:MutableLiveData<User> = MutableLiveData()
    var userName:MutableLiveData<User> = MutableLiveData()

    fun isUser(userData:String){
        Thread{
            userName.postValue(mGetUserByInputData.invoke(userData))
        }.start()
    }

    fun validateUser(email:String,password:String){
        Thread {
            user.postValue(mGetUser.invoke(email, password))
        }.start()
    }

    fun assignCartForUser(){
        Thread{
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
        }.start()
    }
}