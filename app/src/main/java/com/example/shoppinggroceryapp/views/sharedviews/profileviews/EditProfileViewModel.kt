package com.example.shoppinggroceryapp.views.sharedviews.profileviews

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.products.Product
import com.core.domain.user.User
import com.core.usecases.cartusecase.getcartusecase.GetProductsByCartId
import com.core.usecases.orderusecase.getordersusecase.GetPurchasedProducts
import com.core.usecases.userusecase.GetUserByInputData
import com.core.usecases.userusecase.UpdateExistingUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditProfileViewModel(private var mUpdateExistingUser:UpdateExistingUser, private val mGetUserByInputData: GetUserByInputData,
                           private var mGetPurchasedProducts: GetPurchasedProducts, private var mGetProductsByCartId: GetProductsByCartId
):ViewModel() {

    var recentlyBoughtList:MutableLiveData<MutableList<Product>> = MutableLiveData()
    var userEntity:MutableLiveData<User> = MutableLiveData()
    fun saveDetails(oldEmail:String,firstName:String,lastName:String,email:String,phone: String,image:String){
        viewModelScope.launch(Dispatchers.IO) {
            val user1 = mGetUserByInputData.invoke(oldEmail)
            user1?.let {user ->
                val userEntityTmp = User(
                    userId = user.userId,
                    userImage = image,
                    userFirstName = firstName,
                    userLastName = lastName,
                    userEmail = email,
                    userPhone = phone,
                    userPassword = user.userPassword,
                    dateOfBirth = user.dateOfBirth,
                    isRetailer = user.isRetailer)
                mUpdateExistingUser.invoke(userEntityTmp)
            }
        }
//        Thread {
//            val user1 = mGetUserByInputData.invoke(oldEmail)
//            user1?.let {user ->
//                val userEntityTmp = User(
//                    userId = user.userId,
//                    userImage = image,
//                    userFirstName = firstName,
//                    userLastName = lastName,
//                    userEmail = email,
//                    userPhone = phone,
//                    userPassword = user.userPassword,
//                    dateOfBirth = user.dateOfBirth,
//                    isRetailer = user.isRetailer)
//                mUpdateExistingUser.invoke(userEntityTmp)
//            }
//        }.start()
    }

    fun saveUserImage(oldEmail:String,mainImage:String){
        viewModelScope.launch(Dispatchers.IO) {
            val user1 = mGetUserByInputData.invoke(oldEmail)
            user1?.let {user ->
                mUpdateExistingUser.invoke(User(
                    userId = user.userId,
                    userImage = mainImage,
                    userFirstName = user.userFirstName,
                    userLastName = user.userLastName,
                    userEmail = user.userEmail,
                    userPhone = user.userPhone,
                    userPassword = user.userPassword,
                    dateOfBirth = user.dateOfBirth,
                    isRetailer = user.isRetailer
                ))
            }
        }
//        Thread {
//            val user1 = mGetUserByInputData.invoke(oldEmail)
//            user1?.let {user ->
//                mUpdateExistingUser.invoke(User(
//                    userId = user.userId,
//                    userImage = mainImage,
//                    userFirstName = user.userFirstName,
//                    userLastName = user.userLastName,
//                    userEmail = user.userEmail,
//                    userPhone = user.userPhone,
//                    userPassword = user.userPassword,
//                    dateOfBirth = user.dateOfBirth,
//                    isRetailer = user.isRetailer
//                ))
//            }
//        }.start()
    }

    fun getPurchasedProducts(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<Product>()
            for(i in mGetPurchasedProducts.invoke(userId)?: listOf()){
                for(j in mGetProductsByCartId.invoke(i.cartId)){
                    if(j !in list){
                        list.add(j)
                    }
                }
            }
            list.reverse()
            recentlyBoughtList.postValue(list)
        }
//        Thread{
//            val list = mutableListOf<Product>()
//            for(i in mGetPurchasedProducts.invoke(userId)?: listOf()){
//                for(j in mGetProductsByCartId.invoke(i.cartId)){
//                    if(j !in list){
//                        list.add(j)
//                    }
//                }
//            }
//            list.reverse()
//            recentlyBoughtList.postValue(list)
//        }.start()
    }

    fun getUser(emailOrPhone:String){
        viewModelScope.launch(Dispatchers.IO) {
            userEntity.postValue(mGetUserByInputData.invoke(emailOrPhone))
        }
//        Thread{
//            userEntity.postValue(mGetUserByInputData.invoke(emailOrPhone))
//        }.start()
    }

    fun savePassword(user:User){
        viewModelScope.launch(Dispatchers.IO) {
            mUpdateExistingUser.invoke(user)
        }
//        Thread {
//            mUpdateExistingUser.invoke(user)
//        }.start()
    }

    fun resetDetails(sharedPreferences: SharedPreferences){
        val editor = sharedPreferences.edit()
        editor.putInt("selectedAddress",0)
        editor.putBoolean("isSigned",false)
        editor.putBoolean("isRetailer",false)
        editor.putString("userFirstName",null)
        editor.putString("userLastName",null)
        editor.putString("userEmail",null)
        editor.putString("userPhone",null)
        editor.putString("userId",null)
        editor.putString("userProfile",null)
        editor.apply()
    }
}