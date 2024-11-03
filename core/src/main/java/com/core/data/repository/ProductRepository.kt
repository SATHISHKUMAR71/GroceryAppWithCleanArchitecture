package com.core.data.repository

import com.core.data.datasource.productdatasource.ProductDataSource
import com.core.data.datasource.productdatasource.RetailerProductDataSource
import com.core.domain.order.OrderDetails
import com.core.domain.products.BrandData
import com.core.domain.products.CartWithProductData
import com.core.domain.products.Category
import com.core.domain.products.DeletedProductList
import com.core.domain.products.Images
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product
import com.core.domain.products.ProductAndDeletedCounts
import com.core.domain.products.WishList
import com.core.domain.recentlyvieweditems.RecentlyViewedItems
import com.core.domain.user.UserInfoWithOrderInfo

class ProductRepository(private val productDataSource: ProductDataSource,private val retailerProductDataSource: RetailerProductDataSource) {
    fun addProduct(product: Product){
        retailerProductDataSource.addProduct(product)
    }
    fun getOrderInfoForSpecificProduct(productId: Long): List<UserInfoWithOrderInfo>{
        return retailerProductDataSource.getOrderInfoForSpecificProduct(productId)
    }
    fun getAvailableProductsInOrderId(orderId: Int): ProductAndDeletedCounts {
        return retailerProductDataSource.getAvailableProductsInOrderId(orderId)
    }
    fun getMaxPriceInInventory():Float{
        return productDataSource.getMaxPrice()
    }

    fun getSpecificOrder(orderId: Int):OrderDetails{
        return retailerProductDataSource.getSpecificOrder(orderId)
    }
    fun addParentCategory(parentCategory: ParentCategory){
        retailerProductDataSource.addParentCategory(parentCategory)
    }

    fun addToWishList(wishList: WishList) {
        productDataSource.addToWishList(wishList)
    }

    fun getSpecificWishList(userId: Int, productId: Long): WishList? {
        return productDataSource.getSpecificWishList(userId,productId)
    }
    fun getWishedProductsList(userId: Int):List<Product>{
        return productDataSource.getWishedProductsList(userId)
    }

    fun deleteWishList(wishList: WishList) {
        productDataSource.deleteWishList(wishList)
    }

    fun getAllWishList(userId: Int,productId: Long): WishList? {
        return productDataSource.getAllWishList(userId,productId)
    }
    fun addSubCategory(category: Category){
        retailerProductDataSource.addSubCategory(category)
    }

    fun getParentAndChildCategory():Map<ParentCategory,List<Category>>{
        return productDataSource.getParentAndChildNames()
    }

    fun updateAvailableProducts(product:Product){
        productDataSource.updateAvailableProducts(product)
    }

    fun addNewBrand(brandData: BrandData){
        retailerProductDataSource.addNewBrand(brandData)
    }

    fun getLastProduct(): Product?{
        return retailerProductDataSource.getLastProduct()
    }
    fun updateProduct(product: Product){
        retailerProductDataSource.updateProduct(product)
    }

    fun getProductsInRecentList(productId:Long,user:Int): RecentlyViewedItems?{
        return retailerProductDataSource.getProductsInRecentList(productId,user)
    }
    fun getImagesForProduct(productId: Long):List<Images>?{
        return retailerProductDataSource.getImagesForProduct(productId)
    }
    fun getSpecificImage(image:String): Images?{
        return retailerProductDataSource.getSpecificImage(image)
    }
    fun addDeletedProduct(deletedProductList: DeletedProductList){
        return retailerProductDataSource.addDeletedProduct(deletedProductList)
    }
    fun deleteProduct(product: Product){
        retailerProductDataSource.deleteProduct(product)
    }

    fun getBrandWithName(brandName:String): BrandData?{
        return retailerProductDataSource.getBrandWithName(brandName)
    }

    fun getParentCategoryImageForParent(childCategoryName: String):String?{
        println(" #@#@ parent image: got in repo ${retailerProductDataSource.getParentCategoryImageForParent(childCategoryName)}")
        return retailerProductDataSource.getParentCategoryImageForParent(childCategoryName)
    }
    fun getParentCategoryImage(parentCategoryName: String):String?{
        return retailerProductDataSource.getParentCategoryImage(parentCategoryName)
    }
    fun addProductImagesInDb(image: Images){
        retailerProductDataSource.addProductImagesInDb(image)
    }

    fun deleteProductImage(image: Images){
        retailerProductDataSource.deleteProductImage(image)
    }

    fun getParentCategoryName():Array<String>?{
        return retailerProductDataSource.getParentCategoryName()
    }
    fun getParentCategoryNameForChild(childName: String): String? {
        return retailerProductDataSource.getParentCategoryNameForChild(childName)
    }
    fun getChildCategoryName():Array<String>?{
        return retailerProductDataSource.getChildCategoryName()
    }
    fun getChildCategoryName(parentName:String):Array<String>?{
        return retailerProductDataSource.getChildCategoryName(parentName)
    }

    fun getOnlyProducts():List<Product>?{
        return productDataSource.getOnlyProducts()
    }
    fun getProductsById(productId:Long):Product?{
        return productDataSource.getProductById(productId)
    }
    fun getRecentlyViewedProducts(userId: Int): List<Int>? {
        return productDataSource.getRecentlyViewedProducts(userId)
    }
    fun getOfferedProducts():List<Product>?{
        return productDataSource.getOfferedProducts()
    }
    fun getProductByCategory(query:String):List<Product>?{
        return productDataSource.getProductByCategory(query)
    }

    fun getLastlyOrderedProduct(userId: Int,productId: Long):String?{
        return productDataSource.getLastlyOrderedProductDate(userId, productId)
    }
    fun getAllBrands():List<BrandData>{
        return productDataSource.getAllBrands()
    }
    fun getProductsByName(query: String): List<Product>? {
        return productDataSource.getProductsByName(query)
    }
    fun getProductsForQuery(query:String):List<String>?{
        return productDataSource.getProductForQuery(query)
    }
    fun getProductForQueryName(query: String):List<String>?{
        return productDataSource.getProductForQueryName(query)
    }
    fun getProductsByCartId(cartId:Int):List<Product>? {
        return productDataSource.getProductsByCartId(cartId)
    }
    fun getBrandName(id:Long):String?{
        return productDataSource.getBrandName(id)
    }
    fun getDeletedProductsWithCartId(cartId:Int):List<CartWithProductData>? {
        return productDataSource.getDeletedProductsWithCartId(cartId)
    }

    fun getParentCategoryList():List<ParentCategory>?{
        return productDataSource.getParentCategoryList()
    }
    fun getChildName(parent: String): List<String>? {
        return productDataSource.getChildName(parent)
    }
    fun addProductInRecentlyViewedItems(recentlyViewedItem: RecentlyViewedItems){
        productDataSource.addProductInRecentlyViewedItems(recentlyViewedItem)
    }

    fun removeProductInRecentlyViewedItems(recentlyViewedItem: RecentlyViewedItems){
        productDataSource.removeProductInRecentlyViewedItems(recentlyViewedItem)
    }
}