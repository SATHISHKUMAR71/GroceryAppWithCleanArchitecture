package com.core.data.datasource.productdatasource

import com.core.domain.products.BrandData
import com.core.domain.products.CartWithProductData
import com.core.domain.products.Category
import com.core.domain.products.Images
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product
import com.core.domain.products.WishList
import com.core.domain.recentlyvieweditems.RecentlyViewedItems

interface ProductDataSource {
    fun getProductById(productId:Long): Product?
    fun getRecentlyViewedProducts(user:Int):List<Int>?
    fun getOnlyProducts():List<Product>?
    fun getOfferedProducts():List<Product>?
    fun getProductByCategory(query:String):List<Product>?
    fun getProductsByName(query: String):List<Product>?
    fun getProductForQuery(query: String):List<String>?
    fun getProductForQueryName(query: String):List<String>?
    fun getProductsByCartId(cartId:Int):List<Product>?
    fun getBrandName(id:Long):String?
    fun getDeletedProductsWithCartId(cartId:Int):List<CartWithProductData>?
    fun getImagesForProduct(productId: Long):List<Images>?
    fun getParentAndChildNames(): Map<ParentCategory, List<Category>>
    fun getAllBrands():List<BrandData>
    fun addProductInRecentlyViewedItems(recentlyViewedItems: RecentlyViewedItems)
    fun removeProductInRecentlyViewedItems(recentlyViewedItems: RecentlyViewedItems)
    fun getParentCategoryList():List<ParentCategory>?
    fun getChildName(parent:String):List<String>?
    fun getLastlyOrderedProductDate(userId: Int,productId: Long):String?
    fun updateAvailableProducts(product:Product)
    fun addToWishList(wishList: WishList)
    fun deleteWishList(wishList: WishList)
    fun getAllWishList(userId:Int,productId: Long):WishList?
    fun getWishedProductsList(userId: Int):List<Product>
    fun getSpecificWishList(userId:Int,productId:Long):WishList?
    fun getMaxPrice():Float
}