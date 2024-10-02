package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository

class GetChildCategoriesForParent(var retailerRepository: RetailerRepository) {
    fun invoke(parentName:String): Array<String>? {
        return retailerRepository.getChildCategoryName(parentName)
    }
}