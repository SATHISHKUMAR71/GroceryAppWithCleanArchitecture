package com.example.shoppinggroceryapp.framework.db.dataclass

import com.example.shoppinggroceryapp.framework.db.entity.products.CategoryEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ParentCategoryEntity

data class ParentWithChildName(
    val parentCategoryEntity: ParentCategoryEntity,
    val categoryEntityList: List<CategoryEntity>
)