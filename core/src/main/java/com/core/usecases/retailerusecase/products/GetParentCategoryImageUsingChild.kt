package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository

class GetParentCategoryImageUsingChild(private val retailerRepository: RetailerRepository) {
    fun invoke(childName:String):String{
        return retailerRepository.getParentCategoryImageForParent(childName)
    }
}