package com.example.shoppinggroceryapp.framework.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.core.domain.order.OrderDetails
import com.core.domain.products.BrandData
import com.example.shoppinggroceryapp.framework.db.dataclass.CustomerRequestWithName
import com.example.shoppinggroceryapp.framework.db.dataclass.ProductWithDeletedCounts
import com.example.shoppinggroceryapp.framework.db.dataclass.UserInfoWithOrder
import com.example.shoppinggroceryapp.framework.db.entity.order.DailySubscriptionEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.MonthlyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.OrderDetailsEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.TimeSlotEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.WeeklyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.BrandDataEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.CategoryEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.DeletedProductListEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ImagesEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ParentCategoryEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity
import com.example.shoppinggroceryapp.framework.db.entity.recentlyvieweditems.RecentlyViewedItemsEntity

@Dao
interface RetailerDao: UserDao {


    @Query("SELECT UserEntity.userId as userId,UserEntity.userFirstName as userFirstName,UserEntity.userLastName as userLastName,UserEntity.userEmail as userEmail,UserEntity.userPhone as userPhone" +
            ",OrderDetailsEntity.orderId as orderId,OrderDetailsEntity.cartId as cartId,OrderDetailsEntity.addressId as addressId,OrderDetailsEntity.paymentMode as paymentMode,OrderDetailsEntity.deliveryFrequency as deliveryFrequency" +
            ",OrderDetailsEntity.paymentStatus as paymentStatus,OrderDetailsEntity.deliveryStatus as deliveryStatus,OrderDetailsEntity.deliveryDate as deliveryDate,OrderDetailsEntity.orderedDate as orderedDate FROM ORDERDETAILSENTITY JOIN CARTMAPPINGENTITY ON CARTMAPPINGENTITY.cartId=ORDERDETAILSENTITY.cartId JOIN CARTENTITY ON CARTENTITY.cartId=ORDERDETAILSENTITY.cartId JOIN USERENTITY ON USERENTITY.userId=CARTMAPPINGENTITY.userId WHERE CARTENTITY.productId=:productId and (ORDERDETAILSENTITY.deliveryStatus!='Cancelled' or ORDERDETAILSENTITY.deliveryStatus!='Delivered' )")
    fun getOrderInfoForSpecificProduct(productId:Long):List<UserInfoWithOrder>

    @Query("SELECT * FROM ORDERDETAILSENTITY WHERE ORDERDETAILSENTITY.orderId=:orderId")
    fun getSpecificOrder(orderId: Int):OrderDetailsEntity

    @Query("SELECT COUNT(CARTENTITY.productId) as productCount,COUNT(DELETEDPRODUCTLISTENTITY.productId) as deletedProductCount FROM ORDERDETAILSENTITY JOIN CARTENTITY ON ORDERDETAILSENTITY.cartId=CARTENTITY.cartId LEFT JOIN DELETEDPRODUCTLISTENTITY ON DELETEDPRODUCTLISTENTITY.productId=CARTENTITY.productId WHERE ORDERDETAILSENTITY.orderId=:orderId")
    fun getAvailableProductsInOrderId(orderId:Int):ProductWithDeletedCounts

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addParentCategory(parentCategoryEntity: ParentCategoryEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSubCategory(categoryEntity: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewBrand(brandDataEntity: BrandDataEntity)

    @Query("SELECT * FROM DailySubscriptionEntity")
    fun getDailySubscription():List<DailySubscriptionEntity>?

    @Query("SELECT BrandDataEntity.* FROM BrandDataEntity Order by BrandDataEntity.brandName")
    fun getAllBrands():List<BrandData>

    @Query("SELECT * FROM TimeSlotEntity")
    fun getOrderTimeSlot():List<TimeSlotEntity>?

    @Query("SELECT * FROM WeeklyOnceEntity")
    fun getWeeklySubscriptionList():List<WeeklyOnceEntity>?

    @Query("SELECT * FROM MonthlyOnceEntity")
    fun getMonthlySubscriptionList():List<MonthlyOnceEntity>?

    @Query("SELECT CategoryEntity.categoryName FROM CategoryEntity")
    fun getChildCategoryName():Array<String>?

    @Query("SELECT CategoryEntity.parentCategoryName FROM CategoryEntity WHERE CategoryEntity.categoryName=:childName")
    fun getParentCategoryNameForChild(childName:String):String?

    @Query("SELECT ParentCategoryEntity.parentCategoryName FROM ParentCategoryEntity")
    fun getParentCategoryName():Array<String>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM ProductEntity ORDER BY productId DESC")
    fun getLastProduct():ProductEntity?

    @Query("SELECT OrderDetailsEntity.orderedDate FROM OrderDetailsEntity JOIN CartMappingEntity on CartMappingEntity.cartId=OrderDetailsEntity.cartId join CartEntity on CartEntity.cartId=OrderDetailsEntity.cartId where CartMappingEntity.status='not available' and CartEntity.productId=:productId and CartMappingEntity.userId=:userId and (OrderDetailsEntity.deliveryStatus!='Cancelled' or OrderDetailsEntity.deliveryFrequency!='Once') ORDER BY OrderDetailsEntity.orderedDate DESC" )
    fun getLastlyOrderedDate(userId:Int,productId: Long):String?

    @Update
    fun updateProduct(productEntity: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProductInRecentlyViewedItems(recentlyViewedItem: RecentlyViewedItemsEntity)

    @Delete
    fun deleteRecentlyViewedItems(rectItemsEntity: RecentlyViewedItemsEntity)

    @Query("SELECT * FROM RecentlyViewedItemsEntity WHERE RecentlyViewedItemsEntity.productId=:productId and RecentlyViewedItemsEntity.userId=:user")
    fun getProductsInRecentList(productId:Long,user:Int): RecentlyViewedItemsEntity?

    @Query("SELECT * FROM ImagesEntity WHERE ImagesEntity.images=:image")
    fun getSpecificImage(image:String):ImagesEntity?

    @Delete
    fun deleteImage(imagesEntity: ImagesEntity)

    @Query("SELECT * FROM OrderDetailsEntity ORDER BY orderId DESC")
    fun getOrderDetails():List<OrderDetailsEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addImagesInDb(imagesEntity: ImagesEntity)

    @Query("SELECT CustomerRequestEntity.helpId,CustomerRequestEntity.userId,CustomerRequestEntity.requestedDate,CustomerRequestEntity.orderId,CustomerRequestEntity.request,UserEntity.userFirstName,UserEntity.userLastName,UserEntity.userEmail,UserEntity.userPhone FROM CustomerRequestEntity JOIN UserEntity ON UserEntity.userId=CustomerRequestEntity.userId ORDER BY CustomerRequestEntity.helpId DESC")
    fun getDataFromCustomerReqWithName():List<CustomerRequestWithName>?

    @Query("SELECT CustomerRequestEntity.helpId,CustomerRequestEntity.userId,CustomerRequestEntity.requestedDate,CustomerRequestEntity.orderId,CustomerRequestEntity.request,UserEntity.userFirstName,UserEntity.userLastName,UserEntity.userEmail,UserEntity.userPhone FROM CustomerRequestEntity JOIN UserEntity ON UserEntity.userId=CustomerRequestEntity.userId WHERE CustomerRequestEntity.userId=:userId ORDER BY CustomerRequestEntity.helpId DESC")
    fun getCustomerReqWithNameForSpecificCustomer(userId:Int):List<CustomerRequestWithName>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDeletedProduct(deletedProductListEntity: DeletedProductListEntity)

    @Delete
    fun deleteProduct(productEntity: ProductEntity)

    @Query("SELECT CategoryEntity.categoryName FROM CategoryEntity Where CategoryEntity.parentCategoryName=:parentName")
    fun getChildCategoryName(parentName:String):Array<String>?

    @Query("SELECT * FROM BrandDataEntity Where BrandDataEntity.brandName=:brandName")
    fun getBrandWithName(brandName:String): BrandDataEntity?

    @Query("SELECT ParentCategoryEntity.parentCategoryImage FROM ParentCategoryEntity JOIN CategoryEntity ON CategoryEntity.parentCategoryName=ParentCategoryEntity.parentCategoryName Where categoryName=:parentCategoryName")
    fun getParentCategoryImage(parentCategoryName: String):String?

    @Query("SELECT ParentCategoryEntity.parentCategoryImage FROM ParentCategoryEntity Where parentCategoryName=:parentCategoryName")
    fun getParentCategoryImageForParent(parentCategoryName: String):String?

}