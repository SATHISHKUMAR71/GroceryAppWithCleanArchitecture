package com.example.shoppinggroceryapp.framework.data.cart

import com.core.data.datasource.cartdatasource.CartDataSource
import com.core.domain.order.Cart
import com.core.domain.order.CartMapping
import com.core.domain.products.CartWithProductData
import com.core.domain.products.Product
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.order.CartEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.CartMappingEntity

class CartDataSourceImpl(private val userDao: UserDao):CartDataSource {

    override fun getCartForUser(userId: Int): CartMapping? {
        return userDao.getCartForUser(userId)?.let {
            CartMapping(it.cartId,it.userId,it.status)
        }
    }

    override fun addCartForUser(cartMapping: CartMapping) {
        userDao.addCartForUser(CartMappingEntity(cartMapping.cartId,cartMapping.userId,cartMapping.status))
    }

    override fun getCartItems(cartId: Int): List<Cart>? {
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
    }

    override fun getProductsWithCartData(cartId: Int): List<CartWithProductData>? {
        return (userDao.getProductsWithCartData(cartId))?.let { cartDataList ->
            cartDataList.map { CartWithProductData(it.productId,it.mainImage,it.productName,it.productDescription,it.totalItems,it.unitPrice
                ,it.manufactureDate,it.expiryDate,it.productQuantity,it.brandName) }
        }
    }


    override fun getDeletedProductsWithCartId(cartId: Int): List<CartWithProductData>? {
        return userDao.getDeletedProductsWithCartId(cartId)?.let { cartProductList ->
            cartProductList.map { CartWithProductData(it.productId,it.mainImage,it.productName,it.productDescription,it.totalItems,it.unitPrice
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
        val cart = userDao.getSpecificCart(cartId,productId)
        return cart?.let {
            Cart(cart.cartId,cart.productId,cart.totalItems,cart.unitPrice)
        }
    }

    override fun updateCartItems(cart: Cart) {
        userDao.updateCartItems(CartEntity(cart.cartId,cart.productId,cart.totalItems,cart.unitPrice))
    }
}