package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository

class GetAllParentCategoryNames(private val retailerRepository: RetailerRepository) {
    fun invoke():Array<String>{
        return retailerRepository.getParentCategoryName()
    }
}