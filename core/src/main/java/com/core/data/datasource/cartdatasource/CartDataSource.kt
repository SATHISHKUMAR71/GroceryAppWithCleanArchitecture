package com.core.data.datasource.cartdatasource

import com.core.domain.order.Cart
import com.core.domain.order.CartMapping
import com.core.domain.products.CartWithProductData
import com.core.domain.products.Product

interface CartDataSource {

    fun getCartForUser(userId:Int): CartMapping?
    fun addCartForUser(cartMapping:CartMapping)
    fun getCartItems(cartId:Int):List<Cart>?
    fun getProductsByCartId(cartId:Int):List<Product>?
    fun addItemsToCart(cart:Cart)
    fun getProductsWithCartData(cartId:Int):List<CartWithProductData>?
    fun getDeletedProductsWithCartId(cartId:Int):List<CartWithProductData>?
    fun removeProductInCart(cart: Cart)
    fun updateCartMapping(cartMapping: CartMapping)
    fun getSpecificCart(cartId:Int,productId:Int):Cart?
    fun updateCartItems(cart: Cart)
}