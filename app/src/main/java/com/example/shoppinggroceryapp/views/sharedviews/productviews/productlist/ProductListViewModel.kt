package com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.order.Cart
import com.core.domain.products.Product
import com.core.usecases.customerusecase.cart.AddProductInCart
import com.core.usecases.customerusecase.cart.GetCartItems
import com.core.usecases.customerusecase.cart.GetSpecificProductInCart
import com.core.usecases.customerusecase.cart.RemoveProductInCart
import com.core.usecases.customerusecase.cart.UpdateCartItems
import com.core.usecases.customerusecase.products.GetAllProducts
import com.core.usecases.customerusecase.products.GetBrandName
import com.core.usecases.customerusecase.products.GetProductByName
import com.core.usecases.customerusecase.products.GetProductsByCategory
import com.core.usecases.customerusecase.products.GetProductsQueryForName
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.order.CartEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity

class ProductListViewModel(private val mGetProductsByCategory: GetProductsByCategory,
                           private val mGetProductByName: GetProductByName,
                           private val mGetAllProducts: GetAllProducts,
                           private val mAddProductInCart: AddProductInCart,
                           private val mGetSpecificProductInCart: GetSpecificProductInCart,
                           private val mGetBrandName: GetBrandName,
                           private val mRemoveProductInCart: RemoveProductInCart,
                           private val mUpdateCartItems: UpdateCartItems,
                           private val mGetCartItems: GetCartItems):ViewModel() {

    var cartEntityList: MutableLiveData<List<Cart>> = MutableLiveData()
    var productEntityList: MutableLiveData<List<Product>> = MutableLiveData()
    var productEntityCategoryList: MutableLiveData<List<Product>> = MutableLiveData()
    var manufacturedSortedList:MutableLiveData<List<ProductEntity>> = MutableLiveData()
    var cartEntityListForProducts:MutableList<CartEntity?> = mutableListOf()
    fun getCartItems(cartId: Int) {
        Thread {
            cartEntityList.postValue(mGetCartItems.invoke(cartId))
        }.start()
    }

    fun getOnlyProducts() {
        Thread {
            productEntityList.postValue(mGetAllProducts.invoke())
        }.start()
    }


    fun getProductsByCategory(category: String) {
        Thread {

            var list = mGetProductsByCategory.invoke(category)
            if(list?.isEmpty() == true) {
                list = mGetProductByName.invoke(category)
            }
            productEntityCategoryList.postValue(list?: listOf())
        }.start()
    }

    fun getSpecificCart(cartId: Int,productId:Int,callback: (Cart?) -> Unit){
        Thread{
            val cart: Cart? = (mGetSpecificProductInCart.invoke(cartId,productId))
            callback(cart)
        }.start()
    }

    fun addItemsInCart(cart: Cart){
        Thread{
            mAddProductInCart.invoke(cart)
        }.start()
    }
    fun getBrandName(brandId:Long,callbackBrand: (String?) -> Unit){
        Thread{
            callbackBrand(mGetBrandName.invoke(brandId))
        }.start()
    }

    fun removeProductInCart(cart: Cart){
        Thread{
            mRemoveProductInCart.invoke(cart)
        }.start()
    }

    fun updateItemsInCart(cart: Cart){
        Thread{
            println("**** update items in cart called: $cart")
            mAddProductInCart.invoke(cart)
            println("**** update items in cart called: cart items ${mGetCartItems.invoke(1)}")
        }.start()
    }

}