package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.products.WishList

class GetSpecificProductWishlist(var productRepository: ProductRepository) {
    fun invoke(userId:Int,productId:Long): WishList? {
        return productRepository.getSpecificWishList(userId, productId)
    }
}