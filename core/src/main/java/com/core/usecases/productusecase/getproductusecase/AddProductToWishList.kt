package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.products.WishList

class AddProductToWishList(private var productRepository: ProductRepository) {
    fun invoke(wishList: WishList){
        productRepository.addToWishList(wishList)
    }
}