package com.core.usecases.productusecase.retailerproductusecase.getretailerproduct

import com.core.data.repository.ProductRepository

class GetChildCategoryNames(private val productRepository: ProductRepository){
    fun invoke(): Array<String>? {
        return productRepository.getChildCategoryName()
    }
}