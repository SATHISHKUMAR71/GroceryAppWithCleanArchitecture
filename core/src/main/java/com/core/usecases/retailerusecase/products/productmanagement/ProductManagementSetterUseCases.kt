package com.core.usecases.retailerusecase.products.productmanagement

import com.core.usecases.retailerusecase.products.AddNewBrand
import com.core.usecases.retailerusecase.products.AddParentCategory
import com.core.usecases.retailerusecase.products.AddProduct
import com.core.usecases.retailerusecase.products.AddProductImage
import com.core.usecases.retailerusecase.products.AddSubCategory
import com.core.usecases.retailerusecase.products.UpdateProduct

class ProductManagementSetterUseCases(var mAddParentCategory: AddParentCategory,
                                      var mAddSubCategory: AddSubCategory,
                                      var mAddProduct: AddProduct,
                                      var mUpdateProduct: UpdateProduct,
                                      var mAddProductImage: AddProductImage,
                                      var mAddNewBrand: AddNewBrand
)
