package com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.order.Cart
import com.core.domain.products.Product
import com.core.usecases.cartusecase.setcartusecase.AddProductInCart
import com.core.usecases.cartusecase.getcartusecase.GetCartItems
import com.core.usecases.cartusecase.getcartusecase.GetSpecificProductInCart
import com.core.usecases.cartusecase.setcartusecase.RemoveProductInCart
import com.core.usecases.cartusecase.setcartusecase.UpdateCartItems
import com.core.usecases.productusecase.getproductusecase.GetAllProducts
import com.core.usecases.productusecase.getproductusecase.GetBrandName
import com.core.usecases.productusecase.getproductusecase.GetProductByName
import com.core.usecases.productusecase.getproductusecase.GetProductsByCategory
import com.example.shoppinggroceryapp.framework.db.entity.order.CartEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.sort.ProductSorter

class ProductListViewModel(private val mGetProductsByCategory: GetProductsByCategory,
                           private val mGetProductByName: GetProductByName,
                           private val mGetAllProducts: GetAllProducts,
                           private val mAddProductInCart: AddProductInCart,
                           private val mGetSpecificProductInCart: GetSpecificProductInCart,
                           private val mGetBrandName: GetBrandName,
                           private val mRemoveProductInCart: RemoveProductInCart,
                           private val mUpdateCartItems: UpdateCartItems,
                           private val mGetCartItems: GetCartItems
):ViewModel() {

    var cartEntityList: MutableLiveData<List<Cart>> = MutableLiveData()
    var productEntityList: MutableLiveData<List<Product>> = MutableLiveData()
    var productEntityCategoryList: MutableLiveData<List<Product>> = MutableLiveData()
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


    fun doSorting(adapter: ProductListAdapter, it:Int, productEntities:List<Product>, sorter: ProductSorter):List<Product>? {
        var newList: List<Product> = mutableListOf()
        if(it==0){
            if(FilterFragment.list==null) {
                newList = sorter.sortByDate(productEntities)
            }
            else{
                newList = sorter.sortByDate(FilterFragment.list!!)
            }
            adapter.setProducts(newList)
        }
        else if(it == 1){
            if(FilterFragment.list==null) {
                newList = sorter.sortByExpiryDate(productEntities)
            }
            else{
                newList = sorter.sortByExpiryDate(FilterFragment.list!!)
            }
            adapter.setProducts(newList)
        }
        else if(it == 2){
            if(FilterFragment.list==null) {
                newList = sorter.sortByDiscount(productEntities)
            }
            else{
                newList = sorter.sortByDiscount(FilterFragment.list!!)
            }
            adapter.setProducts(newList)
        }
        else if(it == 3){
            if(FilterFragment.list==null) {
                newList = sorter.sortByPriceLowToHigh(productEntities)
            }
            else{
                newList = sorter.sortByPriceLowToHigh(FilterFragment.list!!)
            }
            adapter.setProducts(newList)
        }
        else if(it == 4){
            if(FilterFragment.list==null) {
                newList = sorter.sortByPriceHighToLow(productEntities)
            }
            else{
                newList = sorter.sortByPriceHighToLow(FilterFragment.list!!)
            }
            adapter.setProducts(newList)
        }
        if(newList.isNotEmpty()){
            if(FilterFragment.list!=null){
                if(FilterFragment.list!!.size==newList.size){
                    FilterFragment.list = newList.toMutableList()
                }
            }
            return newList
        }
        return null
    }

}