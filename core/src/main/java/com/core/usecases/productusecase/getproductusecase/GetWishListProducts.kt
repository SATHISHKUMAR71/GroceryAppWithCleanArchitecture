package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class GetWishListProducts (private var productRepository: ProductRepository){
    fun invoke(userId:Int):List<Product>{
        return productRepository.getWishedProductsList(userId)
    }
}