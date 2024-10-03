package com.core.usecases.userusecase

import com.core.data.repository.ProductRepository
import com.core.data.repository.UserRepository
import com.core.domain.products.ParentCategory

class GetParentCategories(private val productRepository: ProductRepository) {
    fun invoke():List<ParentCategory>?{
        return productRepository.getParentCategoryList()
    }
}