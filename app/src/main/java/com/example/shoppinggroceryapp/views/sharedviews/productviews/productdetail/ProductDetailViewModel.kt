package com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.order.Cart
import com.core.domain.products.CartWithProductData
import com.core.domain.products.DeletedProductList
import com.core.domain.products.Images
import com.core.domain.products.Product
import com.core.domain.products.WishList
import com.core.domain.recentlyvieweditems.RecentlyViewedItems
import com.core.domain.user.UserInfoWithOrderInfo
import com.core.usecases.cartusecase.setcartusecase.AddProductInCart
import com.core.usecases.cartusecase.getcartusecase.GetProductsByCartId
import com.core.usecases.cartusecase.getcartusecase.GetProductsWithCartData
import com.core.usecases.cartusecase.getcartusecase.GetSpecificProductInCart
import com.core.usecases.cartusecase.setcartusecase.RemoveProductInCart
import com.core.usecases.cartusecase.setcartusecase.UpdateCartItems
import com.core.usecases.orderusecase.getordersusecase.GetAllOrders
import com.core.usecases.orderusecase.getordersusecase.GetSpecificOrder
import com.core.usecases.orderusecase.updateorderusecase.UpdateOrderDetails
import com.core.usecases.productusecase.getproductusecase.AddProductToWishList
import com.core.usecases.productusecase.getproductusecase.GetAvailableProductsInOrder
import com.core.usecases.productusecase.getproductusecase.GetBrandName
import com.core.usecases.productusecase.getproductusecase.GetImagesForProduct
import com.core.usecases.productusecase.getproductusecase.GetProductsByCategory
import com.core.usecases.productusecase.getproductusecase.GetSpecificProductWishlist
import com.core.usecases.productusecase.getproductusecase.RemoveFromWishList
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddDeletedProductInDb
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.DeleteProduct
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetProductInRecentList
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetUserInfoForModifiedProduct
import com.core.usecases.productusecase.setproductusecase.AddProductInRecentList
import com.core.usecases.productusecase.setproductusecase.RemoveFromRecentlyViewedProducts
import com.example.shoppinggroceryapp.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ProductDetailViewModel(var mDeleteProduct: DeleteProduct,
                             private val mGetBrandName: GetBrandName,
                             private val mGetProductsByCartId: GetProductsByCartId,
                             private val mGetProductInRecentList: GetProductInRecentList,
                             private val mAddProductInRecentList: AddProductInRecentList,
                             private val mGetSpecificProductInCart: GetSpecificProductInCart,
                             private val mGetProductsByCategory: GetProductsByCategory,
                             private val mGetUserInfoForModifiedProduct: GetUserInfoForModifiedProduct,
                             private val mAddProductInCart: AddProductInCart,
                             private val mUpdateCartItems: UpdateCartItems,
                             private val mGetAvailableProductsInOrder: GetAvailableProductsInOrder,
                             private val mRemoveProductInCart: RemoveProductInCart,
                             private var mUpdateOrderDetails: UpdateOrderDetails,
                             private val mGetSpecificOrder: GetSpecificOrder,
                             private val mGetImagesForProduct: GetImagesForProduct,
                             private val mAddProductToWishList: AddProductToWishList,
                             private val mRemoveFromWishList: RemoveFromWishList,
                             private val mGetSpecificProductWishlist: GetSpecificProductWishlist,
                             private val mAddDeletedProductInDb: AddDeletedProductInDb,
                             private val mRemoveFromRecentlyViewedProducts:RemoveFromRecentlyViewedProducts
):ViewModel() {


    var cartProducts:MutableLiveData<List<Product>> = MutableLiveData()
    var brandName:MutableLiveData<String> = MutableLiveData()
    var isCartEntityAvailable:MutableLiveData<Cart> =MutableLiveData()
    var similarProductsLiveData:MutableLiveData<List<Product>> = MutableLiveData()
    var isWishListChecked:MutableLiveData<Boolean> = MutableLiveData()
    var imageList:MutableLiveData<List<Images>> = MutableLiveData()
    var deletedProductUserInfo:MutableLiveData<List<UserInfoWithOrderInfo>> = MutableLiveData()
    var lock = Any()
    companion object{
        var brandLock = Any()
    }

    fun getSpecificProductWishList(userId:Int,productId: Long){
        viewModelScope.launch(Dispatchers.IO){
            if(mGetSpecificProductWishlist.invoke(userId, productId) != null){
                isWishListChecked.postValue(true)
            }
            else{
                isWishListChecked.postValue(false)
            }
        }
    }

    fun addProductToWishList(productId: Long,userId: Int){
        viewModelScope.launch (Dispatchers.IO){
            println("43543 ADDED TOM WISHLIST CALLED: $productId")
            mAddProductToWishList.invoke(WishList(productId, userId))
        }
    }

    fun removeProductFromWishList(productId: Long,userId: Int){
        viewModelScope.launch (Dispatchers.IO){
            println("43543 REMOVE FROM WISHLIST CALLED: $productId")
            mRemoveFromWishList.invoke(WishList(productId, userId))
        }
    }

    fun getBrandName(brandId:Long){
        viewModelScope.launch(Dispatchers.IO) {
            synchronized(brandLock){
                brandName.postValue(mGetBrandName.invoke(brandId))
            }
        }
//        Thread {
//            synchronized(brandLock){
//                brandName.postValue(mGetBrandName.invoke(brandId))
//            }
//        }.start()
    }

    fun getProductsByCartId(cartId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            cartProducts.postValue(mGetProductsByCartId.invoke(cartId))
        }
//        Thread{
//            cartProducts.postValue(mGetProductsByCartId.invoke(cartId))
//        }.start()
    }

    fun addInRecentlyViewedItems(productId: Long){
        viewModelScope.launch (Dispatchers.IO){
            if(mGetProductInRecentList.invoke(productId,MainActivity.userId.toInt())==null) {
                mAddProductInRecentList.invoke((RecentlyViewedItems(0, MainActivity.userId.toInt(),productId)))
            }
            else{
                mRemoveFromRecentlyViewedProducts.invoke(mGetProductInRecentList.invoke(productId,MainActivity.userId.toInt())!!)
                mAddProductInRecentList.invoke((RecentlyViewedItems(0, MainActivity.userId.toInt(),productId)))
            }
        }
//        Thread {
////            mAddProductInRecentList.invoke((RecentlyViewedItems(0, MainActivity.userId.toInt(),productId)))
//            if(mGetProductInRecentList.invoke(productId,MainActivity.userId.toInt())==null) {
//                mAddProductInRecentList.invoke((RecentlyViewedItems(0, MainActivity.userId.toInt(),productId)))
//            }
//            else{
//                mRemoveFromRecentlyViewedProducts.invoke(mGetProductInRecentList.invoke(productId,MainActivity.userId.toInt())!!)
//                mAddProductInRecentList.invoke((RecentlyViewedItems(0, MainActivity.userId.toInt(),productId)))
//            }
//        }.start()
    }

    fun getCartForSpecificProduct(cartId:Int,productId:Int){
        viewModelScope.launch(Dispatchers.IO){
            isCartEntityAvailable.postValue(mGetSpecificProductInCart.invoke(cartId,productId))
        }
//        Thread{
//            isCartEntityAvailable.postValue(mGetSpecificProductInCart.invoke(cartId,productId))
//        }.start()
    }

    fun addProductInCart(cart: Cart){
        viewModelScope.launch(Dispatchers.IO) {
            synchronized(lock){
                mAddProductInCart.invoke(cart)
            }
        }
//        Thread{
//            synchronized(lock){
//                mAddProductInCart.invoke(cart)
//            }
//        }.start()
    }

    fun updateProductInCart(cart: Cart){
        viewModelScope.launch(Dispatchers.IO){
            synchronized(lock){
                mUpdateCartItems.invoke(cart)
            }
        }
//        Thread{
//            synchronized(lock){
//                mUpdateCartItems.invoke(cart)
//            }
//        }.start()
    }

    fun removeProductInCart(cart: Cart){
        viewModelScope.launch(Dispatchers.IO) {
            synchronized(lock){
                mRemoveProductInCart.invoke(cart)
            }
        }
//        Thread{
//            synchronized(lock){
//                mRemoveProductInCart.invoke(cart)
//            }
//        }.start()
    }

    fun getSimilarProduct(category:String){
        viewModelScope.launch(Dispatchers.IO){
            similarProductsLiveData.postValue(mGetProductsByCategory.invoke(category))
        }
//        Thread{
//            similarProductsLiveData.postValue(mGetProductsByCategory.invoke(category))
//        }.start()
    }

    fun getImagesForProducts(productId: Long){
        viewModelScope.launch(Dispatchers.IO){
            imageList.postValue(mGetImagesForProduct.invoke(productId))
        }
//        Thread{
//            imageList.postValue(mGetImagesForProduct.invoke(productId))
//        }.start()
    }
    fun removeProduct(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            mAddDeletedProductInDb.invoke(
                DeletedProductList(productId = product.productId, brandId = product.brandId,
                    categoryName = product.categoryName, productName = product.productName, productDescription = product.productDescription,
                    price = product.price, offer = product.offer, productQuantity = product.productQuantity, mainImage = product.mainImage, isVeg = product.isVeg,
                    manufactureDate = product.manufactureDate, expiryDate = product.expiryDate, availableItems = product.availableItems)
            )
            mDeleteProduct.invoke(product)
        }
//        Thread {
//            mAddDeletedProductInDb.invoke(
//                DeletedProductList(productId = product.productId, brandId = product.brandId,
//                categoryName = product.categoryName, productName = product.productName, productDescription = product.productDescription,
//                price = product.price, offer = product.offer, productQuantity = product.productQuantity, mainImage = product.mainImage, isVeg = product.isVeg,
//                manufactureDate = product.manufactureDate, expiryDate = product.expiryDate, availableItems = product.availableItems)
//            )
//            mDeleteProduct.invoke(product)
//        }.start()
    }

    fun getOrdersForThisProduct(productId:Long){
        viewModelScope.launch(Dispatchers.IO){
            deletedProductUserInfo.postValue(mGetUserInfoForModifiedProduct.invoke(productId))
        }
    }

    fun updateOrders(deletedProductInfo:List<UserInfoWithOrderInfo>){
        viewModelScope.launch(Dispatchers.IO) {
            for (i in deletedProductInfo) {
                println("6564 ORDER ID FOR THE DETAILS ORDER ID: ${mGetAvailableProductsInOrder.invoke(i.orderId)} DELETED INFO: $i")
                val counts = mGetAvailableProductsInOrder.invoke(i.orderId)
                if(counts.productCount==counts.deletedProductCount){
                    println("UPDATING ORDER DETAILS: ")
                    mUpdateOrderDetails.invoke(mGetSpecificOrder.invoke(i.orderId).copy(deliveryStatus = "Cancelled"))
                }
            }
        }
//        Thread {
//            for (i in deletedProductInfo) {
//                println("6564 ORDER ID FOR THE DETAILS ORDER ID: ${mGetAvailableProductsInOrder.invoke(i.orderId)} DELETED INFO: $i")
//                val counts = mGetAvailableProductsInOrder.invoke(i.orderId)
//                if(counts.productCount==counts.deletedProductCount){
//                    println("UPDATING ORDER DETAILS: ")
//                    mUpdateOrderDetails.invoke(mGetSpecificOrder.invoke(i.orderId).copy(deliveryStatus = "Cancelled"))
//                }
//            }
//        }.start()
    }
    fun calculateDiscountPrice(price:Float, offer:Float):Float{
        if(offer>0f) {
            return price - (price * (offer / 100))
        }
        else{
            return price
        }
    }

}