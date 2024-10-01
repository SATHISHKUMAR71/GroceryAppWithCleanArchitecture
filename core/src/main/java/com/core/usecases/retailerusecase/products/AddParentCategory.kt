package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.products.ParentCategory

class AddParentCategory(private val retailerRepository: RetailerRepository) {
    fun invoke(parentCategory: ParentCategory){
        retailerRepository.addParentCategory(parentCategory)
    }
}