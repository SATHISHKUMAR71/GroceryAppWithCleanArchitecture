package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.products.Category
import com.core.domain.products.ParentCategory

class GetParentAndChildCategories(private val productRepository: ProductRepository) {
    fun invoke():Map<ParentCategory,List<Category>>{
        return productRepository.getParentAndChildCategory()
    }
}