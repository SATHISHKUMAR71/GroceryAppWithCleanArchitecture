package com.example.shoppinggroceryapp.views.userviews.cartview.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.products.Product
import com.core.domain.user.Address
import com.core.usecases.addressusecase.GetAllAddress
import com.core.usecases.cartusecase.getcartusecase.GetCartItems
import com.core.usecases.cartusecase.getcartusecase.GetProductsByCartId

class CartViewModel(private val mGetProductsByCartId: GetProductsByCartId,
                    private val mGetCartItems: GetCartItems,
                    private val mGetAllAddress: GetAllAddress
):ViewModel() {

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
            println("CART ITEMS: $list")
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