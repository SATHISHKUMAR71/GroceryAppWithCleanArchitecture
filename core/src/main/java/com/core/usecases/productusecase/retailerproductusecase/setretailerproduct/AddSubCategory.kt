package com.core.usecases.productusecase.retailerproductusecase.setretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.products.Category

class AddSubCategory(private val productRepository: ProductRepository) {
    fun invoke(category: Category){
        productRepository.addSubCategory(category)
    }
}