package com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.order.Cart
import com.core.domain.products.Product
import com.core.domain.products.WishList
import com.core.usecases.cartusecase.setcartusecase.AddProductInCart
import com.core.usecases.cartusecase.getcartusecase.GetCartItems
import com.core.usecases.cartusecase.getcartusecase.GetSpecificProductInCart
import com.core.usecases.cartusecase.setcartusecase.RemoveProductInCart
import com.core.usecases.cartusecase.setcartusecase.UpdateCartItems
import com.core.usecases.productusecase.getproductusecase.AddProductToWishList
import com.core.usecases.productusecase.getproductusecase.GetAllProducts
import com.core.usecases.productusecase.getproductusecase.GetBrandName
import com.core.usecases.productusecase.getproductusecase.GetImagesForProduct
import com.core.usecases.productusecase.getproductusecase.GetProductByName
import com.core.usecases.productusecase.getproductusecase.GetProductsByCategory
import com.core.usecases.productusecase.getproductusecase.GetWishListProducts
import com.core.usecases.productusecase.getproductusecase.GetWishLists
import com.core.usecases.productusecase.getproductusecase.RemoveFromWishList
import com.core.usecases.productusecase.setproductusecase.UpdateAvailableProducts
import com.core.usecases.userusecase.GetLastlyOrderedDateForProduct
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.framework.db.entity.order.CartEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.sort.ProductSorter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListViewModel(private val mGetProductsByCategory: GetProductsByCategory,
                           private val mGetProductByName: GetProductByName,
                           private val mGetAllProducts: GetAllProducts,
                           private val mAddProductInCart: AddProductInCart,
                           private val mUpdateAvailableProducts: UpdateAvailableProducts,
                           private val mGetSpecificProductInCart: GetSpecificProductInCart,
                           private val mGetBrandName: GetBrandName,
                           private val mRemoveProductInCart: RemoveProductInCart,
                           private val mUpdateCartItems: UpdateCartItems,
                           private val mGetAllImagesForProduct: GetImagesForProduct,
                           private val mGetWishListProducts:GetWishListProducts,
                           private val mAddProductToWishList: AddProductToWishList,
                           private val mGetWishLists: GetWishLists,
                           private val mRemoveFromWishList: RemoveFromWishList,
                           private val mGetLastlyOrderedDate:GetLastlyOrderedDateForProduct,
                           private val mGetCartItems: GetCartItems
):ViewModel() {

    var cartEntityList: MutableLiveData<List<Cart>> = MutableLiveData()
    var productEntityList: MutableLiveData<List<Product>> = MutableLiveData()
    var productEntityCategoryList: MutableLiveData<List<Product>> = MutableLiveData()
    fun getCartItems(cartId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cartEntityList.postValue(mGetCartItems.invoke(cartId))
        }
//        Thread {
//            cartEntityList.postValue(mGetCartItems.invoke(cartId))
//        }.start()
    }

    fun getOnlyProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            productEntityList.postValue(mGetAllProducts.invoke())
        }
//        Thread {
//            productEntityList.postValue(mGetAllProducts.invoke())
//        }.start()
    }

    fun getImagesCountForProduct(productId: Long,callback:(Int) -> Unit){
        viewModelScope.launch(Dispatchers.IO){
            val size = mGetAllImagesForProduct.invoke(productId)?.size?:0
            callback(size)
        }
    }
    fun getProductsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var list = mGetProductsByCategory.invoke(category)
            if(list?.isEmpty() == true) {
                list = mGetProductByName.invoke(category)
            }
            productEntityCategoryList.postValue(list?: listOf())
        }
//        Thread {
//            var list = mGetProductsByCategory.invoke(category)
//            if(list?.isEmpty() == true) {
//                list = mGetProductByName.invoke(category)
//            }
//            productEntityCategoryList.postValue(list?: listOf())
//        }.start()
    }

    fun getSpecificCart(cartId: Int,productId:Int,callback: (Cart?) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            val cart: Cart? = (mGetSpecificProductInCart.invoke(cartId,productId))
            callback(cart)
        }
//        Thread{
//            val cart: Cart? = (mGetSpecificProductInCart.invoke(cartId,productId))
//            callback(cart)
//        }.start()
    }

    fun getWishList(productId: Long,callback: (WishList?) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            callback(mGetWishLists.invoke(MainActivity.userId.toInt(),productId))
        }
//        Thread{
//            callback(mGetWishLists.invoke(MainActivity.userId.toInt(),productId))
//        }.start()
    }


    fun getLastlyOrderedDate(userId: Int,productId: Long,callback:(String?)->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            callback(mGetLastlyOrderedDate.invoke(userId,productId))
        }
//        Thread{
//            callback(mGetLastlyOrderedDate.invoke(userId,productId))
//        }.start()
    }
    fun addItemsInCart(cart: Cart){
        viewModelScope.launch(Dispatchers.IO) {
            mAddProductInCart.invoke(cart)
        }
//        Thread{
//            mAddProductInCart.invoke(cart)
//        }.start()
    }
    fun getBrandName(brandId:Long,callbackBrand: (String?) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            callbackBrand(mGetBrandName.invoke(brandId))
        }
//        Thread{
//            callbackBrand(mGetBrandName.invoke(brandId))
//        }.start()
    }

    fun removeProductInCart(cart: Cart){
        viewModelScope.launch(Dispatchers.IO) {
            mRemoveProductInCart.invoke(cart)
        }
//        Thread{
//            mRemoveProductInCart.invoke(cart)
//        }.start()
    }

    fun updateItemsInCart(cart: Cart){
        viewModelScope.launch(Dispatchers.IO) {
            mAddProductInCart.invoke(cart)
        }
//        Thread{
//            mAddProductInCart.invoke(cart)
//        }.start()
    }

    fun updateProductInInventory(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            mUpdateAvailableProducts.invoke(product)
        }
//        Thread{
//            mUpdateAvailableProducts.invoke(product)
//        }.start()
    }

    fun getWishedProducts(userId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            productEntityList.postValue(mGetWishListProducts.invoke(userId))
        }
//        Thread{
//            productEntityList.postValue(mGetWishListProducts.invoke(userId))
//        }.start()
    }

    fun addProductToWishList(productId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            mAddProductToWishList.invoke(WishList(productId,MainActivity.userId.toInt()))
        }
//        Thread{
//            mAddProductToWishList.invoke(WishList(productId,MainActivity.userId.toInt()))
//        }.start()
    }

    fun removeProductFromWishList(productId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            mRemoveFromWishList.invoke(WishList(productId,MainActivity.userId.toInt()))
        }
//        Thread{
//            mRemoveFromWishList.invoke(WishList(productId,MainActivity.userId.toInt()))
//        }.start()
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