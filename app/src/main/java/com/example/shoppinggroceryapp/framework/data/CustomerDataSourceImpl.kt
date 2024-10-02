package com.example.shoppinggroceryapp.framework.data

import com.core.data.datasource.customerdatasource.CustomerDataSource
import com.core.domain.help.CustomerRequest
import com.core.domain.order.Cart
import com.core.domain.order.CartMapping
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.OrderDetails
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
import com.core.domain.products.CartWithProductData
import com.core.domain.products.Images
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product
import com.core.domain.user.Address
import com.core.domain.user.User
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.help.CustomerRequestEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.CartEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.CartMappingEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.DailySubscriptionEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.MonthlyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.TimeSlotEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.WeeklyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ParentCategoryEntity
import com.example.shoppinggroceryapp.framework.db.entity.user.AddressEntity

class CustomerDataSourceImpl(private var userDao: UserDao):CustomerDataSource,ConvertionHelper() {
    override fun addNewUser(user: User) {
        userDao.addUser(convertUserToUserEntity(user))
    }

    override fun updateUser(user: User) {
        userDao.updateUser(convertUserToUserEntity(user))
    }

    override fun updateAddress(address: Address) {
        userDao.updateAddress(
            AddressEntity(address.addressId,address.userId,address.addressContactName,
            address.addressContactNumber,address.buildingName,address.streetName,address.city,address.state,address.country,address.postalCode)
        )
    }

    override fun addCustomerRequest(customerRequest: CustomerRequest) {
        userDao.addCustomerRequest(
            CustomerRequestEntity(customerRequest.helpId,customerRequest.userId,customerRequest.requestedDate,
            customerRequest.orderId,customerRequest.request)
        )
    }

    override fun getUser(emailOrPhone: String, password: String): User? {
        return userDao.getUser(emailOrPhone,password)?.let {
            convertUserEntityToUser(it)
        }
    }

    override fun getUserData(emailOrPhone: String): User? {

        return userDao.getUserData(emailOrPhone)?.let {
            convertUserEntityToUser(it)
        }

    }

    override fun getOrderDetails(orderId: Int): OrderDetails? {
        return userDao.getOrderDetails(orderId)?.let {
            convertOrderEntityToOrderDetails(it)
        }
    }

    override fun getCartForUser(userId: Int): CartMapping? {
        return userDao.getCartForUser(userId)?.let {
            CartMapping(it.cartId,it.userId,it.status)
        }
    }

    override fun addCartForUser(cartMapping: CartMapping) {
        userDao.addCartForUser(CartMappingEntity(cartMapping.cartId,cartMapping.userId,cartMapping.status))
    }

    override fun getCartItems(cartId: Int): List<Cart>? {
        println("**** update items in cart called: in db ${userDao.getCartItems(cartId)} $cartId")
        return userDao.getCartItems(cartId)?.let {cartList ->
            cartList.map { Cart(it.cartId,it.productId,it.totalItems,it.unitPrice) }
        }
    }

    override fun getProductsByCartId(cartId: Int): List<Product>? {
        return userDao.getProductsByCartId(cartId)?.let { productList ->
            productList.map { Product(it.productId,it.brandId,it.categoryName,it.productName,it.productDescription
            ,it.price,it.offer,it.productQuantity,it.mainImage,it.isVeg,it.manufactureDate,it.expiryDate,it.availableItems) }
        }
    }

    override fun addItemsToCart(cart: Cart) {
        userDao.addItemsToCart(CartEntity(cart.cartId,cart.productId,cart.totalItems,cart.unitPrice))
        println("**** update items in cart called: Added Items to Cart $cart ${getCartItems(cart.cartId)}")
    }

    override fun getProductsWithCartData(cartId: Int): List<CartWithProductData>? {
        return (userDao.getProductsWithCartData(cartId))?.let { cartDataList ->
            cartDataList.map { CartWithProductData(it.mainImage,it.productName,it.productDescription,it.totalItems,it.unitPrice
            ,it.manufactureDate,it.expiryDate,it.productQuantity,it.brandName) }
        }
    }


    override fun getDeletedProductsWithCartId(cartId: Int): List<CartWithProductData>? {
        return userDao.getDeletedProductsWithCartId(cartId)?.let { cartProductList ->
            cartProductList.map { CartWithProductData(it.mainImage,it.productName,it.productDescription,it.totalItems,it.unitPrice
            ,it.manufactureDate,it.expiryDate,it.productQuantity,it.brandName) }
        }

    }

    override fun removeProductInCart(cart: Cart) {
        userDao.removeProductInCart(CartEntity(cart.cartId,cart.productId,cart.totalItems,cart.unitPrice))
    }

    override fun updateCartMapping(cartMapping: CartMapping) {
        userDao.updateCartMapping(CartMappingEntity(cartMapping.cartId,cartMapping.userId,cartMapping.status))
    }

    override fun getSpecificCart(cartId: Int, productId: Int): Cart? {
        var cart = userDao.getSpecificCart(cartId,productId)
        println("CART 0909: $cart $cartId $productId")
        return cart?.let {
            Cart(cart.cartId,cart.productId,cart.totalItems,cart.unitPrice)
        }
    }

    override fun updateCartItems(cart: Cart) {
        userDao.updateCartItems(CartEntity(cart.cartId,cart.productId,cart.totalItems,cart.unitPrice))
    }

    override fun getBoughtProductsList(userId: Int): List<OrderDetails>? {
        return userDao.getBoughtProductsList(userId)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUser(userID: Int): List<OrderDetails>? {
        return userDao.getOrdersForUser(userID)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserWeeklySubscription(userID: Int): List<OrderDetails>? {
        return userDao.getOrdersForUserWeeklySubscription(userID)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserDailySubscription(userID: Int): List<OrderDetails>? {
        return userDao.getOrdersForUserDailySubscription(userID)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserMonthlySubscription(userID: Int): List<OrderDetails>? {
        return userDao.getOrdersForUserMonthlySubscription(userID)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }

    override fun getOrdersForUserNoSubscription(userID: Int): List<OrderDetails>? {
        return userDao.getOrdersForUserNoSubscription(userID)?.let {
            convertOrderDetailsEntityToOrderDetails(it)
        }
    }


    override fun getOrder(cartId: Int): OrderDetails? {

        return userDao.getOrder(cartId)?.let {
            convertOrderEntityToOrderDetails(it)
        }
    }

    override fun addOrder(order: OrderDetails): Long? {
        return userDao.addOrder(convertOrderDetailsToOrderDetailsEntity(order))
    }

    override fun updateOrderDetails(orderDetails: OrderDetails) {
        userDao.updateOrderDetails(convertOrderDetailsToOrderDetailsEntity(orderDetails))
    }

    override fun getOrderWithProductsWithOrderId(orderId: Int): Map<OrderDetails, List<CartWithProductData>>? {
        val map:MutableMap<OrderDetails, List<CartWithProductData>> = mutableMapOf()

        userDao.getOrderWithProductsWithOrderId(orderId)?.let {orderDetailsMap ->
            for( i in orderDetailsMap){
                map[convertOrderEntityToOrderDetails(i.key)] = i.value.map { CartWithProductData(it.mainImage,it.productName,it.productDescription,it.totalItems,it.unitPrice,
                    it.manufactureDate,it.expiryDate,it.productQuantity,it.brandName) }
            }
        }
        return map
    }

    override fun getOrderDetailsWithOrderId(orderId: Int): OrderDetails? {
        return userDao.getOrderDetails(orderId)?.let {
            convertOrderEntityToOrderDetails(it)
        }
    }

    override fun getProductById(productId: Long): Product? {
        return userDao.getProductById(productId)?.let {
            convertProductEntityToProduct(it)
        }
    }

    override fun getRecentlyViewedProducts(user: Int): List<Int>? {
        return userDao.getRecentlyViewedProducts(user)
    }

    override fun getOnlyProducts(): List<Product>? {
        return userDao.getOnlyProducts()?.map { convertProductEntityToProduct(it) }
    }

    override fun getOfferedProducts(): List<Product>? {
        return userDao.getOfferedProducts()?.map { convertProductEntityToProduct(it) }
    }

    override fun getProductByCategory(query: String): List<Product>? {
        return userDao.getProductByCategory(query)?.map { convertProductEntityToProduct(it) }
    }

    override fun getProductsByName(query: String): List<Product>? {
        return userDao.getProductsByName(query)?.map { convertProductEntityToProduct(it) }
    }

    override fun getProductForQuery(query: String): List<String>? {
        return userDao.getProductForQuery(query)
    }

    override fun getProductForQueryName(query: String): List<String>? {
        return userDao.getProductForQueryName(query)
    }

    override fun getBrandName(id: Long): String? {
        return userDao.getBrandName(id)
    }

    override fun updateParentCategory(parentCategory: ParentCategory) {
        userDao.updateParentCategory(ParentCategoryEntity(parentCategory.parentCategoryName,parentCategory.parentCategoryImage,parentCategory.parentCategoryDescription,parentCategory.isEssential))
    }

    override fun getImagesForProduct(productId: Long): List<Images>? {
        return userDao.getImagesForProduct(productId)?.map { Images(it.imageId,it.productId,it.images) }
    }

    override fun getAddress(addressId: Int): Address? {
        return userDao.getAddress(addressId)?.let {address ->
            Address(address.addressId,address.userId,address.addressContactName,address.addressContactNumber,address.buildingName
                ,address.streetName,address.city,address.state,address.country,address.postalCode)
        }
    }

    override fun addAddress(address: Address) {
        userDao.addAddress(AddressEntity(address.addressId,address.userId,address.addressContactName,address.addressContactNumber,address.buildingName
            ,address.streetName,address.city,address.state,address.country,address.postalCode))
    }

    override fun getAddressListForUser(userId: Int): List<Address>? {
        return userDao.getAddressListForUser(userId)?.map { address -> Address(address.addressId,address.userId,address.addressContactName,address.addressContactNumber,address.buildingName
            ,address.streetName,address.city,address.state,address.country,address.postalCode) }
    }

    override fun addTimeSlot(timeSlot: TimeSlot) {
        userDao.addTimeSlot(TimeSlotEntity(timeSlot.orderId,timeSlot.timeId))
    }

    override fun addMonthlyOnceSubscription(monthlyOnce: MonthlyOnce) {
        userDao.addMonthlyOnceSubscription(MonthlyOnceEntity(monthlyOnce.orderId,monthlyOnce.dayOfMonth))
    }

    override fun addWeeklyOnceSubscription(weeklyOnce: WeeklyOnce) {
        userDao.addWeeklyOnceSubscription(WeeklyOnceEntity(weeklyOnce.orderId,weeklyOnce.weekId))
    }

    override fun addDailySubscription(dailySubscription: DailySubscription) {
        userDao.addDailySubscription(DailySubscriptionEntity(dailySubscription.orderId))
    }

}