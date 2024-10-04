package com.core.usecases.productusecase.retailerproductusecase.getretailerproduct

import com.core.data.repository.ProductRepository

class GetAllParentCategoryNames(private val productRepository: ProductRepository) {
    fun invoke():Array<String>?{
        return productRepository.getParentCategoryName()
    }
}