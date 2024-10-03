package com.core.usecases.retailerusecase.products

import com.core.data.repository.ProductRepository
import com.core.domain.products.ParentCategory

class AddParentCategory(private val productRepository: ProductRepository) {
    fun invoke(parentCategory: ParentCategory){
        productRepository.addParentCategory(parentCategory)
    }
}