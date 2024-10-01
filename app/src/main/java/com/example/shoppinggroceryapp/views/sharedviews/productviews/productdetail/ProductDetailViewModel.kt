package com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.order.Cart
import com.core.domain.products.DeletedProductList
import com.core.domain.products.Images
import com.core.domain.products.Product
import com.core.domain.recentlyvieweditems.RecentlyViewedItems
import com.core.usecases.customerusecase.cart.AddProductInCart
import com.core.usecases.customerusecase.cart.GetProductsByCartId
import com.core.usecases.customerusecase.cart.GetSpecificProductInCart
import com.core.usecases.customerusecase.cart.RemoveProductInCart
import com.core.usecases.customerusecase.cart.UpdateCartItems
import com.core.usecases.customerusecase.products.GetBrandName
import com.core.usecases.customerusecase.products.GetImagesForProduct
import com.core.usecases.customerusecase.products.GetProductsByCategory
import com.core.usecases.customerusecase.products.GetRecentlyViewedProducts
import com.core.usecases.retailerusecase.products.AddDeletedProductInDb
import com.core.usecases.retailerusecase.products.DeleteProduct
import com.core.usecases.retailerusecase.products.GetProductInRecentList
import com.core.usecases.userusecase.AddProductInRecentList
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.entity.order.CartEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.DeletedProductListEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ImagesEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity
import com.example.shoppinggroceryapp.framework.db.entity.recentlyvieweditems.RecentlyViewedItemsEntity

class ProductDetailViewModel(var mDeleteProduct: DeleteProduct,
                             private val mGetBrandName: GetBrandName,
                             private val mGetProductsByCartId: GetProductsByCartId,
                             private val mGetProductInRecentList: GetProductInRecentList,
                             private val mAddProductInRecentList: AddProductInRecentList,
                             private val mGetSpecificProductInCart: GetSpecificProductInCart,
                             private val mGetProductsByCategory: GetProductsByCategory,
                             private val mAddProductInCart: AddProductInCart,
                             private val mUpdateCartItems: UpdateCartItems,
                             private val mRemoveProductInCart: RemoveProductInCart,
                             private val mGetImagesForProduct: GetImagesForProduct,
                             private val mAddDeletedProductInDb: AddDeletedProductInDb):ViewModel() {


    var cartProducts:MutableLiveData<List<Product>> = MutableLiveData()
    var brandName:MutableLiveData<String> = MutableLiveData()
    var isCartEntityAvailable:MutableLiveData<Cart> =MutableLiveData()
    var similarProductsLiveData:MutableLiveData<List<Product>> = MutableLiveData()
    var imageList:MutableLiveData<List<Images>> = MutableLiveData()
    var lock = Any()
    companion object{
        var brandLock = Any()
    }
    fun getBrandName(brandId:Long){
        Thread {
            synchronized(brandLock){
                brandName.postValue(mGetBrandName.invoke(brandId))
            }
        }.start()
    }

    fun getProductsByCartId(cartId:Int){
        Thread{
            cartProducts.postValue(mGetProductsByCartId.invoke(cartId))
        }.start()
    }

    fun addInRecentlyViewedItems(productId: Long){
        Thread {

            if(mGetProductInRecentList.invoke(productId,MainActivity.userId.toInt())==null) {
                mAddProductInRecentList.invoke((RecentlyViewedItems(0, MainActivity.userId.toInt(),productId)))
            }
        }.start()
    }

    fun getCartForSpecificProduct(cartId:Int,productId:Int){
        Thread{
            isCartEntityAvailable.postValue(mGetSpecificProductInCart.invoke(cartId,productId))
        }.start()
    }

    fun addProductInCart(cart: Cart){
        Thread{
            synchronized(lock){
                mAddProductInCart.invoke(cart)
            }
        }.start()
    }

    fun updateProductInCart(cart: Cart){
        Thread{
            synchronized(lock){
                mUpdateCartItems.invoke(cart)
            }
        }.start()
    }

    fun removeProductInCart(cart: Cart){
        Thread{
            synchronized(lock){
                mRemoveProductInCart.invoke(cart)
            }
        }.start()
    }

    fun getSimilarProduct(category:String){
        Thread{

            similarProductsLiveData.postValue(mGetProductsByCategory.invoke(category))
        }.start()
    }

    fun getImagesForProducts(productId: Long){
        Thread{

            imageList.postValue(mGetImagesForProduct.invoke(productId))
        }.start()
    }
    fun removeProduct(product: Product){
        Thread {
            mAddDeletedProductInDb.invoke(
                DeletedProductList(productId = product.productId, brandId = product.brandId,
                categoryName = product.categoryName, productName = product.productName, productDescription = product.productDescription,
                price = product.price, offer = product.offer, productQuantity = product.productQuantity, mainImage = product.mainImage, isVeg = product.isVeg,
                manufactureDate = product.manufactureDate, expiryDate = product.expiryDate, availableItems = product.availableItems)
            )
            mDeleteProduct.invoke(product)
        }.start()
    }

}