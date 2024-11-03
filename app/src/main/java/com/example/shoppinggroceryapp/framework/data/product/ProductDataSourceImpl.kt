package com.example.shoppinggroceryapp.framework.data.product

import android.provider.ContactsContract.CommonDataKinds.Im
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
import com.example.shoppinggroceryapp.framework.data.ConvertorHelper
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.dataclass.ProductWithDeletedCounts
import com.example.shoppinggroceryapp.framework.db.entity.products.BrandDataEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.CategoryEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.DeletedProductListEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ImagesEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ParentCategoryEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.WishListEntity
import com.example.shoppinggroceryapp.framework.db.entity.recentlyvieweditems.RecentlyViewedItemsEntity

class ProductDataSourceImpl(private val retailerDao: RetailerDao):ProductDataSource,RetailerProductDataSource {
    var convertorHelper = ConvertorHelper()
    override fun getProductById(productId: Long): Product? {
        return retailerDao.getProductById(productId)?.let {
            convertorHelper.convertProductEntityToProduct(it)
        }
    }

    override fun getRecentlyViewedProducts(user: Int): List<Int>? {
        return retailerDao.getRecentlyViewedProducts(user)
    }

    override fun getOnlyProducts(): List<Product>? {
        return retailerDao.getOnlyProducts()?.map { convertorHelper.convertProductEntityToProduct(it) }
    }

    override fun getOfferedProducts(): List<Product>? {
        return retailerDao.getOfferedProducts()?.map { convertorHelper.convertProductEntityToProduct(it) }
    }

    override fun getProductByCategory(query: String): List<Product>? {
        return retailerDao.getProductByCategory(query)?.map { convertorHelper.convertProductEntityToProduct(it) }
    }

    override fun getProductsByName(query: String): List<Product>? {
        return retailerDao.getProductsByName(query)?.map { convertorHelper.convertProductEntityToProduct(it) }
    }

    override fun getProductForQuery(query: String): List<String>? {
        return retailerDao.getProductForQuery(query)
    }

    override fun getProductForQueryName(query: String): List<String>? {
        return retailerDao.getProductForQueryName(query)
    }

    override fun getBrandName(id: Long): String? {
        return retailerDao.getBrandName(id)
    }

    override fun getProductsByCartId(cartId: Int): List<Product>? {
        return retailerDao.getProductsByCartId(cartId)?.let { productList ->
            productList.map { Product(it.productId,it.brandId,it.categoryName,it.productName,it.productDescription
                ,it.price,it.offer,it.productQuantity,it.mainImage,it.isVeg,it.manufactureDate,it.expiryDate,it.availableItems) }
        }
    }
    override fun getDeletedProductsWithCartId(cartId: Int): List<CartWithProductData>? {
        return retailerDao.getDeletedProductsWithCartId(cartId)?.let { cartProductList ->
            cartProductList.map { CartWithProductData(it.productId,it.mainImage,it.productName,it.productDescription,it.totalItems,it.unitPrice
                ,it.manufactureDate,it.expiryDate,it.productQuantity,it.brandName) }
        }

    }

    override fun addProduct(product: Product) {
        retailerDao.addProduct(convertorHelper.convertProductToProductEntity(product))
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
        return retailerDao.getLastProduct()?.let { convertorHelper.convertProductEntityToProduct(it) }
    }

    override fun updateProduct(product: Product) {
        retailerDao.updateProduct(convertorHelper.convertProductToProductEntity(product))
    }

    override fun getProductsInRecentList(productId: Long, userId: Int): RecentlyViewedItems? {
        return retailerDao.getProductsInRecentList(productId,userId)?.let {
            RecentlyViewedItems(it.recentlyViewedId,it.userId,it.productId)
        }
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
        retailerDao.deleteProduct(convertorHelper.convertProductToProductEntity(product))
    }

    override fun getBrandWithName(brandName: String): BrandData? {
        return retailerDao.getBrandWithName(brandName)?.let {
            BrandData(it.brandId,it.brandName)
        }
    }

    override fun getParentCategoryImageForParent(parentCategoryName: String): String? {
        println("#@#@ parent image: $parentCategoryName")
        println("#@#@ parent image: got ${retailerDao.getParentCategoryImageForParent(parentCategoryName)} ${retailerDao.getParentCategoryImage(parentCategoryName)}")
        return retailerDao.getParentCategoryImage(parentCategoryName)
    }

    override fun getParentCategoryImage(parentCategoryName: String): String? {
        println("GET PARENT IMAGE CALLED: in impl ${retailerDao.getParentCategoryImage(parentCategoryName)} ${retailerDao.getParentCategoryImageForParent(parentCategoryName)}")
        return retailerDao.getParentCategoryImageForParent(parentCategoryName)
    }

    override fun addProductImagesInDb(image: Images) {
        image.apply {
            retailerDao.addImagesInDb(ImagesEntity(imageId,productId,images))
        }
    }

    override fun deleteProductImage(image: Images) {
        image.apply {
            retailerDao.deleteImage(ImagesEntity(imageId,productId, images))
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

    override fun getOrderInfoForSpecificProduct(productId: Long): List<UserInfoWithOrderInfo> {
        return retailerDao.getOrderInfoForSpecificProduct(productId).map { UserInfoWithOrderInfo(it.userId,it.userFirstName,
            it.userLastName,
            it.userEmail,
            it.userPhone,
            it.orderId,
            it.cartId,
            it.addressId,
            it.paymentMode,
            it.deliveryFrequency,
            it.paymentStatus,
            it.deliveryStatus,
            it.deliveryDate,
            it.orderedDate)}
    }

    override fun getAvailableProductsInOrderId(orderId: Int): ProductAndDeletedCounts {
        return retailerDao.getAvailableProductsInOrderId(orderId).let { ProductAndDeletedCounts(it.productCount,it.deletedProductCount) }
    }

    override fun getSpecificOrder(orderId: Int): OrderDetails {
        return convertorHelper.convertOrderEntityToOrderDetails(retailerDao.getSpecificOrder(orderId))
    }

    override fun getParentAndChildNames(): Map<ParentCategory, List<Category>> {
        val mapData = mutableMapOf<ParentCategory,List<Category>>()
        for (i in retailerDao.getParentAndChildNames()){
            mapData[ParentCategory(i.key.parentCategoryName,i.key.parentCategoryImage,i.key.parentCategoryDescription,i.key.isEssential)] = i.value.map { Category(it.categoryName,it.parentCategoryName,it.categoryDescription) }
        }
        return mapData
    }

    override fun getAllBrands(): List<BrandData> {
        return retailerDao.getAllBrands()
    }

    override fun addProductInRecentlyViewedItems(recentlyViewedItems: RecentlyViewedItems) {
        retailerDao.addProductInRecentlyViewedItems(RecentlyViewedItemsEntity(recentlyViewedItems.recentlyViewedId,recentlyViewedItems.userId,recentlyViewedItems.productId))
    }

    override fun removeProductInRecentlyViewedItems(recentlyViewedItems: RecentlyViewedItems) {
        retailerDao.deleteRecentlyViewedItems(RecentlyViewedItemsEntity(recentlyViewedItems.recentlyViewedId,recentlyViewedItems.userId,recentlyViewedItems.productId))
    }

    override fun getParentCategoryList(): List<ParentCategory>? {
        return  retailerDao.getParentCategoryList()?.map { ParentCategory(it.parentCategoryName,it.parentCategoryImage,it.parentCategoryDescription,it.isEssential) }
    }

    override fun getChildName(parent: String): List<String>? {
        return retailerDao.getChildName(parent)?.map { it.categoryName }
    }

    override fun getLastlyOrderedProductDate(userId: Int, productId: Long): String? {
        return retailerDao.getLastlyOrderedDate(userId,productId)
    }

    override fun updateAvailableProducts(product: Product) {
        retailerDao.updateProductAvailable(convertorHelper.convertProductToProductEntity(product))
        println("434324 Product Updated: $product")
    }

    override fun addToWishList(wishList: WishList) {
        retailerDao.addToWishList(WishListEntity(wishList.productId,wishList.userId))
    }

    override fun deleteWishList(wishList: WishList) {
        retailerDao.deleteFromWishList(WishListEntity(wishList.productId,wishList.userId))
    }

    override fun getAllWishList(userId: Int,productId: Long): WishList? {
        retailerDao.getWishLists(userId,productId)?.let {
            return WishList(it.productId,it.userId)
        }
        return null
    }

    override fun getWishedProductsList(userId: Int): List<Product> {
        return retailerDao.getWishedProductList(userId).map { convertorHelper.convertProductEntityToProduct(it) }
    }

    override fun getSpecificWishList(userId: Int, productId: Long): WishList? {
        val wishList = retailerDao.getSpecificProductInWishList(userId,productId)
        wishList?.let {
            return WishList(it.productId,it.userId)
        }
        return null
    }

    override fun getMaxPrice(): Float {
        return retailerDao.getMaxPrice().price
    }

    override fun getImagesForProduct(productId: Long): List<Images>? {
        return retailerDao.getImagesForProduct(productId)?.map { Images(it.imageId,it.productId,it.images) }
    }
}