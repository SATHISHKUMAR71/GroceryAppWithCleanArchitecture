package com.core.data.repository

import com.core.data.datasource.productdatasource.ProductDataSource
import com.core.data.datasource.productdatasource.ProductRetailerDataSource
import com.core.domain.products.BrandData
import com.core.domain.products.CartWithProductData
import com.core.domain.products.Category
import com.core.domain.products.DeletedProductList
import com.core.domain.products.Images
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product
import com.core.domain.recentlyvieweditems.RecentlyViewedItems

class ProductRepository(private val productDataSource: ProductDataSource,private val productRetailerDataSource: ProductRetailerDataSource) {
    fun addProduct(product: Product){
        productRetailerDataSource.addProduct(product)
    }

    fun addParentCategory(parentCategory: ParentCategory){
        productRetailerDataSource.addParentCategory(parentCategory)
    }

    fun addSubCategory(category: Category){
        productRetailerDataSource.addSubCategory(category)
    }

    fun addNewBrand(brandData: BrandData){
        productRetailerDataSource.addNewBrand(brandData)
    }

    fun getLastProduct(): Product?{
        return productRetailerDataSource.getLastProduct()
    }
    fun updateProduct(product: Product){
        productRetailerDataSource.updateProduct(product)
    }

    fun getProductsInRecentList(productId:Long,user:Int): RecentlyViewedItems?{
        return productRetailerDataSource.getProductsInRecentList(productId,user)
    }
    fun getImagesForProduct(productId: Long):List<Images>?{
        return productRetailerDataSource.getImagesForProduct(productId)
    }
    fun getSpecificImage(image:String): Images?{
        return productRetailerDataSource.getSpecificImage(image)
    }
    fun addDeletedProduct(deletedProductList: DeletedProductList){
        return productRetailerDataSource.addDeletedProduct(deletedProductList)
    }
    fun deleteProduct(product: Product){
        productRetailerDataSource.deleteProduct(product)
    }

    fun getBrandWithName(brandName:String): BrandData?{
        return productRetailerDataSource.getBrandWithName(brandName)
    }

    fun getParentCategoryImageForParent(childCategoryName: String):String?{
        return productRetailerDataSource.getParentCategoryImageForParent(childCategoryName)
    }
    fun getParentCategoryImage(parentCategoryName: String):String?{
        return productRetailerDataSource.getParentCategoryImage(parentCategoryName)
    }
    fun addProductImagesInDb(image: Images){
        productRetailerDataSource.addProductImagesInDb(image)
    }

    fun deleteProductImage(image: Images){
        productRetailerDataSource.deleteProductImage(image)
    }

    fun getParentCategoryName():Array<String>?{
        return productRetailerDataSource.getParentCategoryName()
    }
    fun getParentCategoryNameForChild(childName: String): String? {
        return productRetailerDataSource.getParentCategoryNameForChild(childName)
    }
    fun getChildCategoryName():Array<String>?{
        return productRetailerDataSource.getChildCategoryName()
    }
    fun getChildCategoryName(parentName:String):Array<String>?{
        return productRetailerDataSource.getChildCategoryName(parentName)
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

}