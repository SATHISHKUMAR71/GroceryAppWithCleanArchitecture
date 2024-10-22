package com.core.data.datasource.productdatasource

import com.core.domain.order.OrderDetails
import com.core.domain.products.BrandData
import com.core.domain.products.Category
import com.core.domain.products.DeletedProductList
import com.core.domain.products.Images
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product
import com.core.domain.products.ProductAndDeletedCounts
import com.core.domain.recentlyvieweditems.RecentlyViewedItems
import com.core.domain.user.UserInfoWithOrderInfo

interface RetailerProductDataSource {
    fun addProduct(product: Product)
    fun addParentCategory(parentCategory: ParentCategory)
    fun addSubCategory(category: Category)
    fun addNewBrand(brandData: BrandData)
    fun getLastProduct():Product?
    fun updateProduct(product: Product)
    fun getProductsInRecentList(productId:Long,user:Int):RecentlyViewedItems?
    fun getImagesForProduct(productId: Long):List<Images>?
    fun getSpecificImage(image:String):Images?
    fun addDeletedProduct(deletedProductList: DeletedProductList)
    fun deleteProduct(product: Product)
    fun getBrandWithName(brandName:String):BrandData?
    fun getParentCategoryImageForParent(parentCategoryName: String):String?
    fun getParentCategoryImage(parentCategoryName: String):String?
    fun addProductImagesInDb(image: Images)
    fun deleteProductImage(image: Images)
    fun getParentCategoryName():Array<String>?
    fun getParentCategoryNameForChild(childName:String):String?
    fun getChildCategoryName():Array<String>?
    fun getChildCategoryName(parentName:String):Array<String>?
    fun getOrderInfoForSpecificProduct(productId:Long):List<UserInfoWithOrderInfo>
    fun getAvailableProductsInOrderId(orderId:Int):ProductAndDeletedCounts
    fun getSpecificOrder(orderId: Int):OrderDetails
}
