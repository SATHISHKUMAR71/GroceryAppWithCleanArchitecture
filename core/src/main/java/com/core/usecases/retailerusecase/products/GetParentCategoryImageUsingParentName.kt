package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository

class GetParentCategoryImageUsingParentName(private val retailerRepository: RetailerRepository) {
    fun invoke(parentCategoryName:String):String{
        return retailerRepository.getParentCategoryImage(parentCategoryName)
    }
}