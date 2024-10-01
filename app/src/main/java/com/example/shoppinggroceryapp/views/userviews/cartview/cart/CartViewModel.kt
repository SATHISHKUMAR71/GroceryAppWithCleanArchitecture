package com.example.shoppinggroceryapp.views.userviews.cartview.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.products.Product
import com.core.domain.user.Address
import com.core.usecases.customerusecase.address.GetAllAddress
import com.core.usecases.customerusecase.cart.GetCartItems
import com.core.usecases.customerusecase.cart.GetProductsByCartId
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity
import com.example.shoppinggroceryapp.framework.db.entity.user.AddressEntity

class CartViewModel(private val mGetProductsByCartId: GetProductsByCartId,
                    private val mGetCartItems: GetCartItems,
                    private val mGetAllAddress: GetAllAddress):ViewModel() {

    var cartProducts:MutableLiveData<List<Product>> = MutableLiveData()
    var totalPrice:MutableLiveData<Float> = MutableLiveData()
    var addressEntityList:MutableLiveData<List<Address>> = MutableLiveData()
    fun getProductsByCartId(cartId:Int){
        Thread{

            cartProducts.postValue(mGetProductsByCartId.invoke(cartId))
        }.start()
    }


    fun calculateInitialPrice(cartId: Int){
        Thread{
            val list = mGetCartItems.invoke(cartId)
            var price = 49f
            for(i in list){
                price += (i.unitPrice*i.totalItems)
            }
            totalPrice.postValue(price)
        }.start()
    }

    fun getAddressListForUser(userId:Int){
        Thread{

            addressEntityList.postValue(mGetAllAddress.invoke(userId))
        }.start()
    }
}