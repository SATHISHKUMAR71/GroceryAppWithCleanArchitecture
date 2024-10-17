package com.core.usecases.userusecase

import com.core.data.repository.ProductRepository

class GetLastlyOrderedDateForProduct(var productRepository: ProductRepository){
    fun invoke(userId:Int,productId:Long):String?{
        return productRepository.getLastlyOrderedProduct(userId, productId)
    }
}