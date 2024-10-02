package com.core.data.repository

import com.core.data.datasource.customerdatasource.CustomerDataSource
import com.core.data.datasource.retailerdatasource.ProductRetailerDataSource
import com.core.data.datasource.retailerdatasource.RetailerDataSource
import com.core.domain.help.CustomerRequestWithName
import com.core.domain.order.OrderDetails
import com.core.domain.products.BrandData
import com.core.domain.products.Category
import com.core.domain.products.DeletedProductList
import com.core.domain.products.Images
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product
import com.core.domain.recentlyvieweditems.RecentlyViewedItems

class RetailerRepository(private var retailerDataSource: RetailerDataSource){

    fun addProduct(product: Product){
        retailerDataSource.addProduct(product)
    }

    fun addParentCategory(parentCategory: ParentCategory){
        retailerDataSource.addParentCategory(parentCategory)
    }

    fun addSubCategory(category: Category){
        retailerDataSource.addSubCategory(category)
    }

    fun addNewBrand(brandData: BrandData){
        retailerDataSource.addNewBrand(brandData)
    }

    fun getLastProduct():Product?{
        return retailerDataSource.getLastProduct()
    }
    fun updateProduct(product: Product){
        retailerDataSource.updateProduct(product)
    }

    fun getProductsInRecentList(productId:Long,user:Int):RecentlyViewedItems?{
        return retailerDataSource.getProductsInRecentList(productId,user)
    }
    fun getImagesForProduct(productId: Long):List<Images>?{
        return retailerDataSource.getImagesForProduct(productId)
    }
    fun getSpecificImage(image:String):Images?{
        return retailerDataSource.getSpecificImage(image)
    }
    fun addDeletedProduct(deletedProductList: DeletedProductList){
        return retailerDataSource.addDeletedProduct(deletedProductList)
    }
    fun deleteProduct(product: Product){
        retailerDataSource.deleteProduct(product)
    }

    fun getBrandWithName(brandName:String):BrandData?{
        return retailerDataSource.getBrandWithName(brandName)
    }

    fun getParentCategoryImageForParent(childCategoryName: String):String?{
        return retailerDataSource.getParentCategoryImageForParent(childCategoryName)
    }
    fun getParentCategoryImage(parentCategoryName: String):String?{
        return retailerDataSource.getParentCategoryImage(parentCategoryName)
    }
    fun addProductImagesInDb(image: Images){
        retailerDataSource.addProductImagesInDb(image)
    }

    fun deleteProductImage(image: Images){
        retailerDataSource.deleteProductImage(image)
    }

    fun getParentCategoryName():Array<String>?{
        return retailerDataSource.getParentCategoryName()
    }
    fun getParentCategoryNameForChild(childName: String): String? {
        return retailerDataSource.getParentCategoryNameForChild(childName)
    }
    fun getChildCategoryName():Array<String>?{
        return retailerDataSource.getChildCategoryName()
    }
    fun getChildCategoryName(parentName:String):Array<String>?{
        return retailerDataSource.getChildCategoryName(parentName)
    }

    fun getDataFromCustomerReqWithName():List<CustomerRequestWithName>?{
        return retailerDataSource.getDataFromCustomerReqWithName()
    }

    fun getOrdersForRetailerWeeklySubscription(): List<OrderDetails>? {
        return retailerDataSource.getOrdersForRetailerWeeklySubscription()
    }

    fun getOrdersRetailerDailySubscription(): List<OrderDetails>? {
        return retailerDataSource.getOrdersRetailerDailySubscription()
    }

    fun getOrdersForRetailerMonthlySubscription(): List<OrderDetails>? {
        return retailerDataSource.getOrdersForRetailerMonthlySubscription()
    }

    fun getOrdersForRetailerNoSubscription(): List<OrderDetails>? {
        return retailerDataSource.getOrdersForRetailerNoSubscription()
    }

    fun getAllOrders():List<OrderDetails>?{
        return retailerDataSource.getAllOrders()
    }
}