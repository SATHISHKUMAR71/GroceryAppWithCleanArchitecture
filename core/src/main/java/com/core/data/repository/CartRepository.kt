package com.core.data.repository

import com.core.data.datasource.cartdatasource.CartDataSource
import com.core.domain.order.Cart
import com.core.domain.order.CartMapping
import com.core.domain.products.CartWithProductData
import com.core.domain.products.Product

class CartRepository(private val cartDataSource: CartDataSource) {
    fun getCartForUser(userId:Int): CartMapping? {
        return cartDataSource.getCartForUser(userId)
    }
    fun addCartForUser(cartMapping:CartMapping){
        cartDataSource.addCartForUser(cartMapping)
    }
    fun getCartItems(cartId:Int):List<Cart>? {
        return cartDataSource.getCartItems(cartId)
    }
    fun getProductsByCartId(cartId:Int):List<Product>? {
        return cartDataSource.getProductsByCartId(cartId)
    }
    fun addItemsToCart(cart:Cart){
        cartDataSource.addItemsToCart(cart)
    }
    fun getProductsWithCartData(cartId:Int):List<CartWithProductData>? {
        return cartDataSource.getProductsWithCartData(cartId)
    }
    fun getDeletedProductsWithCartId(cartId:Int):List<CartWithProductData>? {
        return cartDataSource.getDeletedProductsWithCartId(cartId)
    }
    fun removeProductInCart(cart: Cart){
        return cartDataSource.removeProductInCart(cart)
    }
    fun updateCartMapping(cartMapping: CartMapping){
        cartDataSource.updateCartMapping(cartMapping)
    }
    fun getSpecificCart(cartId:Int,productId:Int):Cart?{
        return cartDataSource.getSpecificCart(cartId,productId)
    }

    fun updateCartItems(cart: Cart){
        return cartDataSource.updateCartItems(cart)
    }
}