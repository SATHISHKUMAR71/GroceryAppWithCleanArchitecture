package com.core.usecases.productusecase.retailerproductusecase.setretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.products.ParentCategory

class AddParentCategory(private val productRepository: ProductRepository) {
    fun invoke(parentCategory: ParentCategory){
        productRepository.addParentCategory(parentCategory)
    }
}