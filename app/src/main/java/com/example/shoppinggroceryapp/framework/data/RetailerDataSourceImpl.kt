package com.example.shoppinggroceryapp.framework.data

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
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.entity.products.BrandDataEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.CategoryEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.DeletedProductListEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ImagesEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ParentCategoryEntity

class RetailerDataSourceImpl(private var retailerDao: RetailerDao):RetailerDataSource,ConvertorHelper(){

    override fun addProduct(product: Product) {
        retailerDao.addProduct(convertProductToProductEntity(product))
    }

    override fun addParentCategory(parentCategory: ParentCategory) {
        retailerDao.addParentCategory(ParentCategoryEntity(parentCategory.parentCategoryName,parentCategory.parentCategoryImage,parentCategory.parentCategoryDescription,parentCategory.isEssential))
    }

    override fun addSubCategory(category: Category) {
        retailerDao.addSubCategory(CategoryEntity(category.categoryName,category.parentCategoryName,category.categoryDescription))
    }

    override fun addNewBrand(brandData: BrandData) {
        retailerDao.addNewBrand(BrandDataEntity(brandData.brandId,brandData.brandName))
    }

    override fun getLastProduct(): Product? {
        return retailerDao.getLastProduct()?.let { convertProductEntityToProduct(it) }
    }

    override fun updateProduct(product: Product) {
        retailerDao.updateProduct(convertProductToProductEntity(product))
    }

    override fun getProductsInRecentList(productId: Long, userId: Int): RecentlyViewedItems? {
        return retailerDao.getProductsInRecentList(productId,userId)?.let {
            RecentlyViewedItems(it.recentlyViewedId,it.userId,it.productId)
        }
    }

    override fun getImagesForProduct(productId: Long): List<Images>? {
        return retailerDao.getImagesForProduct(productId)?.map { Images(it.imageId,it.productId,it.images) }
    }

    override fun getSpecificImage(image: String): Images? {
        return retailerDao.getSpecificImage(image)?.let{
            Images(it.imageId,it.productId,it.images)
        }
    }

    override fun addDeletedProduct(deletedProductList: DeletedProductList) {
        deletedProductList.apply {
            retailerDao.addDeletedProduct(DeletedProductListEntity(productId,brandId,categoryName,productName,productDescription,price,offer,productQuantity, mainImage, isVeg, manufactureDate, expiryDate, availableItems))
        }
    }

    override fun deleteProduct(product: Product) {
        retailerDao.deleteProduct(convertProductToProductEntity(product))
    }

    override fun getBrandWithName(brandName: String): BrandData? {
        return retailerDao.getBrandWithName(brandName)?.let {
            BrandData(it.brandId,it.brandName)
        }
    }

    override fun getParentCategoryImageForParent(parentCategoryName: String): String? {
        return retailerDao.getParentCategoryImageForParent(parentCategoryName)
    }

    override fun getParentCategoryImage(parentCategoryName: String): String? {
        return retailerDao.getParentCategoryImage(parentCategoryName)
    }

    override fun addProductImagesInDb(image: Images) {
        image.apply {
            retailerDao.addImagesInDb(ImagesEntity(imageId,productId,images))
        }
    }

    override fun deleteProductImage(image: Images) {
        image.apply {
            retailerDao.addImagesInDb(ImagesEntity(imageId,productId, images))
        }
    }

    override fun getParentCategoryName(): Array<String>? {
        return retailerDao.getParentCategoryName()
    }

    override fun getParentCategoryNameForChild(childName: String): String? {
        return retailerDao.getParentCategoryNameForChild(childName)
    }

    override fun getChildCategoryName(): Array<String>? {
        return retailerDao.getChildCategoryName()
    }

    override fun getChildCategoryName(parentName: String): Array<String>? {
        return retailerDao.getChildCategoryName(parentName)
    }

    override fun getDataFromCustomerReqWithName(): List<CustomerRequestWithName>? {
        return retailerDao.getDataFromCustomerReqWithName()?.map { CustomerRequestWithName(it.helpId,it.userId,it.requestedDate,it.orderId,it.request,it.userFirstName,it.userLastName,it.userEmail,it.userPhone) }
    }
    override fun getOrdersForRetailerWeeklySubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersForRetailerWeeklySubscription()?.let { convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getOrdersRetailerDailySubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersRetailerDailySubscription()
            ?.let { convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getOrdersForRetailerMonthlySubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersForRetailerMonthlySubscription()
            ?.let { convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getOrdersForRetailerNoSubscription(): List<OrderDetails>? {
        return retailerDao.getOrdersForRetailerNoSubscription()
            ?.let { convertOrderDetailsEntityToOrderDetails(it) }
    }

    override fun getAllOrders(): List<OrderDetails>? {
        return retailerDao.getOrderDetails()?.let { convertOrderDetailsEntityToOrderDetails(it) }
    }
}