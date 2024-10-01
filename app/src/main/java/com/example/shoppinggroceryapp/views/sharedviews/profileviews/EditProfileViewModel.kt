package com.example.shoppinggroceryapp.views.sharedviews.profileviews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.products.Product
import com.core.domain.user.User
import com.core.usecases.customerusecase.cart.GetProductsByCartId
import com.core.usecases.customerusecase.cart.GetProductsWithCartData
import com.core.usecases.customerusecase.orders.GetPurchasedProducts
import com.core.usecases.userusecase.GetUser
import com.core.usecases.userusecase.GetUserByInputData
import com.core.usecases.userusecase.UpdateExistingUser
import com.example.shoppinggroceryapp.framework.db.dao.UserDao


class EditProfileViewModel(private var mUpdateExistingUser:UpdateExistingUser,private val mGetUserByInputData: GetUserByInputData,
    private var mGetPurchasedProducts: GetPurchasedProducts, private var mGetProductsByCartId: GetProductsByCartId
):ViewModel() {

    var recentlyBoughtList:MutableLiveData<MutableList<Product>> = MutableLiveData()
    var userEntity:MutableLiveData<User> = MutableLiveData()
    fun saveDetails(oldEmail:String,firstName:String,lastName:String,email:String,phone: String,image:String){
        Thread {

            val user = mGetUserByInputData.invoke(oldEmail)
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
        }.start()
    }

    fun saveUserImage(oldEmail:String,mainImage:String){
        Thread {
            val user = mGetUserByInputData.invoke(oldEmail)
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
        }.start()
    }

    fun getPurchasedProducts(userId:Int){
        Thread{
            val list = mutableListOf<Product>()
            for(i in mGetPurchasedProducts.invoke(userId)){
                for(j in mGetProductsByCartId.invoke(i.cartId)){
                    if(j !in list){
                        list.add(j)
                    }
                }
            }
            list.reverse()
            recentlyBoughtList.postValue(list)
        }.start()
    }

    fun getUser(emailOrPhone:String){
        Thread{
            userEntity.postValue(mGetUserByInputData.invoke(emailOrPhone))
        }.start()
    }

    fun savePassword(user:User){
        Thread {
            mUpdateExistingUser.invoke(user)
        }.start()
    }
}