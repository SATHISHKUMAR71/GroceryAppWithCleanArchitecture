package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository

class GetParentCategoryNameForChild(private val retailerRepository: RetailerRepository) {
    fun invoke(childName:String):String{
        return retailerRepository.getParentCategoryNameForChild(childName)
    }
}