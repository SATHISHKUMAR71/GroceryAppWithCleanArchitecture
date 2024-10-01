package com.core.usecases.retailerusecase.products.productmanagement

import com.core.usecases.customerusecase.products.GetBrandName
import com.core.usecases.customerusecase.products.GetImagesForProduct
import com.core.usecases.retailerusecase.products.GetAllParentCategoryNames
import com.core.usecases.retailerusecase.products.GetBrandWithName
import com.core.usecases.retailerusecase.products.GetChildCategoriesForParent
import com.core.usecases.retailerusecase.products.GetChildCategoryNames
import com.core.usecases.retailerusecase.products.GetImage
import com.core.usecases.retailerusecase.products.GetLastProduct
import com.core.usecases.retailerusecase.products.GetParentCategoryImageUsingChild
import com.core.usecases.retailerusecase.products.GetParentCategoryImageUsingParentName
import com.core.usecases.retailerusecase.products.GetParentCategoryNameForChild

data class ProductManagementGetterUseCases(var mGetBrandName: GetBrandName,
                                           var mGetAllParentCategoryNames: GetAllParentCategoryNames,
                                           var mGetParentCategoryNameForChild: GetParentCategoryNameForChild,
                                           var mGetChildCategoryNames: GetChildCategoryNames,
                                           var mGetParentCategoryImageUsingChild: GetParentCategoryImageUsingChild,
                                           var mGetParentCategoryImageUsingParentName: GetParentCategoryImageUsingParentName,
                                           var mGetChildCategoriesForParent: GetChildCategoriesForParent,
                                           var mGetImagesForProduct: GetImagesForProduct,
                                           var mGetBandWithName: GetBrandWithName,
                                           var mGetLastProduct: GetLastProduct,
                                           var mGetImage: GetImage
)