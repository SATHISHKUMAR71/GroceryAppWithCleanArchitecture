package com.example.shoppinggroceryapp.views.userviews.cartview.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.products.Product
import com.core.domain.user.Address
import com.core.usecases.addressusecase.GetAllAddress
import com.core.usecases.cartusecase.getcartusecase.GetCartItems
import com.core.usecases.cartusecase.getcartusecase.GetProductsByCartId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(private val mGetProductsByCartId: GetProductsByCartId,
                    private val mGetCartItems: GetCartItems,
                    private val mGetAllAddress: GetAllAddress
):ViewModel() {

    var cartProducts:MutableLiveData<MutableList<Product>> = MutableLiveData()
    var totalPrice:MutableLiveData<Float> = MutableLiveData()
    var addressEntityList:MutableLiveData<List<Address>> = MutableLiveData()
    fun getProductsByCartId(cartId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            cartProducts.postValue(mGetProductsByCartId.invoke(cartId).toMutableList())
        }
//        Thread{
//            cartProducts.postValue(mGetProductsByCartId.invoke(cartId).toMutableList())
//        }.start()
    }


    fun calculateInitialPrice(cartId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val list = mGetCartItems.invoke(cartId)
            var price = 49f
            println("CART ITEMS: $list")
            for(i in list){
                price += (i.unitPrice*i.totalItems)
            }
            totalPrice.postValue(price)
        }
//        Thread{
//            val list = mGetCartItems.invoke(cartId)
//            var price = 49f
//            println("CART ITEMS: $list")
//            for(i in list){
//                price += (i.unitPrice*i.totalItems)
//            }
//            totalPrice.postValue(price)
//        }.start()
    }

    fun getAddressListForUser(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            addressEntityList.postValue(mGetAllAddress.invoke(userId))
        }
//        Thread{
//            addressEntityList.postValue(mGetAllAddress.invoke(userId))
//        }.start()
    }
}