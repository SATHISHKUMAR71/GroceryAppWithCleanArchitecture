package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.products.Category

class AddSubCategory(private val retailerRepository: RetailerRepository) {
    fun invoke(category: Category){
        retailerRepository.addSubCategory(category)
    }
}