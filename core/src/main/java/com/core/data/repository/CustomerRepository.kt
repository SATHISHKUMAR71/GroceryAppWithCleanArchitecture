package com.core.data.repository

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
import com.core.domain.products.Product
import com.core.domain.search.SearchHistory
import com.core.domain.user.Address
import com.core.domain.user.User

open class CustomerRepository(private var customerDataSource: CustomerDataSource) {

    fun addNewUser(user: User) {
        customerDataSource.addNewUser(user)
    }

    fun updateExistingUser(user: User) {
        customerDataSource.updateUser(user)
    }

    fun getProductByCategory(query:String):List<Product>?{
        return customerDataSource.getProductByCategory(query)
    }

    fun getRecentlyViewedProducts(userId: Int): List<Int>? {
        return customerDataSource.getRecentlyViewedProducts(userId)
    }

    fun addNewAddress(address: Address) {
        customerDataSource.addAddress(address)
    }

    fun updateAddress(address: Address) {
        customerDataSource.updateAddress(address)
    }


    fun getAddressListForUser(userId: Int): List<Address>? {
        return customerDataSource.getAddressListForUser(userId)
    }

    fun addCustomerRequest(customerRequest: CustomerRequest) {
        customerDataSource.addCustomerRequest(customerRequest)
    }

    fun getOnlyProducts():List<Product>?{
        return customerDataSource.getOnlyProducts()
    }

    fun getUser(emailOrPhone:String,password:String):User?{
        return customerDataSource.getUser(emailOrPhone,password)
    }

    fun getProductsById(productId:Long):Product?{
        return customerDataSource.getProductById(productId)
    }

    fun getUserData(emailOrPhone: String):User?{
        return customerDataSource.getUserData(emailOrPhone)
    }

    fun getOfferedProducts():List<Product>?{
        return customerDataSource.getOfferedProducts()
    }
    fun getProductsForQuery(query:String):List<String>?{
        return customerDataSource.getProductForQuery(query)
    }

    fun getProductsByName(query: String): List<Product>? {
        return customerDataSource.getProductsByName(query)
    }

    fun getProductForQueryName(query: String):List<String>?{
        return customerDataSource.getProductForQueryName(query)
    }
    fun getAddress(addressId:Int):Address?{
        return customerDataSource.getAddress(addressId)
    }

    fun getCartForUser(userId:Int): CartMapping? {
        return customerDataSource.getCartForUser(userId)
    }
    fun addCartForUser(cartMapping:CartMapping){
        customerDataSource.addCartForUser(cartMapping)
    }
    fun getBoughtProductsList(userId: Int):List<OrderDetails>?{
        return customerDataSource.getBoughtProductsList(userId)
    }
    fun getCartItems(cartId:Int):List<Cart>? {
        return customerDataSource.getCartItems(cartId)
    }
    fun getProductsByCartId(cartId:Int):List<Product>? {
        return customerDataSource.getProductsByCartId(cartId)
    }
    fun addItemsToCart(cart:Cart){
        customerDataSource.addItemsToCart(cart)
    }
    fun getProductsWithCartData(cartId:Int):List<CartWithProductData>? {
        return customerDataSource.getProductsWithCartData(cartId)
    }
    fun getDeletedProductsWithCartId(cartId:Int):List<CartWithProductData>? {
        return customerDataSource.getDeletedProductsWithCartId(cartId)
    }
    fun getOrdersForUser(userID:Int):List<OrderDetails>? {
        return customerDataSource.getOrdersForUser(userID)
    }
    fun getOrdersForUserWeeklySubscription(userID:Int):List<OrderDetails>? {
        return customerDataSource.getOrdersForUserWeeklySubscription(userID)
    }
    fun getOrdersForUserDailySubscription(userID:Int):List<OrderDetails>? {
        return customerDataSource.getOrdersForUserDailySubscription(userID)
    }

    fun getOrdersForUserMonthlySubscription(userID:Int):List<OrderDetails>? {
        return customerDataSource.getOrdersForUserMonthlySubscription(userID)
    }

    fun getOrdersForUserNoSubscription(userID:Int):List<OrderDetails>? {
        return customerDataSource.getOrdersForUserNoSubscription(userID)
    }

    fun getOrder(cartId:Int):OrderDetails?{
        return customerDataSource.getOrder(cartId)
    }
    fun getOrderWithProductsWithOrderId(orderId: Int):Map<OrderDetails,List<CartWithProductData>>?{
        return customerDataSource.getOrderWithProductsWithOrderId(orderId)
    }

    fun getOrderDetails(orderId:Int):OrderDetails?{
        return customerDataSource.getOrderDetails(orderId)
    }
    fun removeProductInCart(cart: Cart){
        return customerDataSource.removeProductInCart(cart)
    }

    fun getBrandName(id:Long):String?{
        return customerDataSource.getBrandName(id)
    }

    fun updateCartMapping(cartMapping: CartMapping){
        customerDataSource.updateCartMapping(cartMapping)
    }

    fun getSpecificCart(cartId:Int,productId:Int):Cart?{
        return customerDataSource.getSpecificCart(cartId,productId)
    }

    fun updateCartItems(cart: Cart){
        return customerDataSource.updateCartItems(cart)
    }
    fun getImagesForProduct(productId: Long):List<Images>?{
        return customerDataSource.getImagesForProduct(productId)
    }
    fun addOrder(order: OrderDetails): Long? {
        return customerDataSource.addOrder(order)
    }
    fun addTimeSlot(timeSlot: TimeSlot) {
        customerDataSource.addTimeSlot(timeSlot)
    }

    fun addMonthlyOnceSubscription(monthlyOnce: MonthlyOnce) {
        customerDataSource.addMonthlyOnceSubscription(monthlyOnce)
    }

    fun addWeeklyOnceSubscription(weeklyOnce: WeeklyOnce) {
        customerDataSource.addWeeklyOnceSubscription(weeklyOnce)
    }

    fun addDailySubscription(dailySubscription: DailySubscription) {
        customerDataSource.addDailySubscription(dailySubscription)
    }

    fun updateOrderDetails(orderDetails: OrderDetails){
        customerDataSource.updateOrderDetails(orderDetails)
    }
}