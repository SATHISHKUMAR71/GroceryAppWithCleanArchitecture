package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.products.WishList

class RemoveFromWishList(private var productRepository: ProductRepository) {
    fun invoke(wishList: WishList){
        productRepository.deleteWishList(wishList)
    }
}