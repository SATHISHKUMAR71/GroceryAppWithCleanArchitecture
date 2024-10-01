package com.core.data.datasource.customerdatasource

import com.core.domain.products.CartWithProductData
import com.core.domain.products.Images
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product

interface ProductDataSource {
    fun getProductById(productId:Long): Product
    fun getRecentlyViewedProducts(user:Int):List<Int>
    fun getOnlyProducts():List<Product>
    fun getOfferedProducts():List<Product>
    fun getProductByCategory(query:String):List<Product>
    fun getProductsByName(query: String):List<Product>
    fun getProductForQuery(query: String):List<String>
    fun getProductForQueryName(query: String):List<String>
    fun getProductsByCartId(cartId:Int):List<Product>
    fun getBrandName(id:Long):String
    fun getDeletedProductsWithCartId(cartId:Int):List<CartWithProductData>
    fun updateParentCategory(parentCategory: ParentCategory)
    fun getImagesForProduct(productId: Long):List<Images>
}