package com.core.usecases.productusecase.retailerproductusecase.getretailerproduct

import com.core.data.repository.ProductRepository

class GetChildCategoriesForParent(var productRepository: ProductRepository) {
    fun invoke(parentName:String): Array<String>? {
        return productRepository.getChildCategoryName(parentName)
    }
}