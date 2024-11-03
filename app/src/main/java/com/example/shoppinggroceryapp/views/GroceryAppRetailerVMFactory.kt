package com.example.shoppinggroceryapp.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.core.data.repository.CartRepository
import com.core.data.repository.HelpRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.usecases.cartusecase.getcartusecase.GetProductsWithCartData
import com.core.usecases.cartusecase.getcartusecase.GetSpecificProductInCart
import com.core.usecases.helpusecase.GetCustomerReqForSpecificUser
import com.core.usecases.orderusecase.getordersusecase.GetOrderDetailsWithOrderId
import com.core.usecases.productusecase.getproductusecase.GetBrandName
import com.core.usecases.productusecase.getproductusecase.GetImagesForProduct
import com.core.usecases.helpusecase.GetCustomerRequestWithName
import com.core.usecases.productusecase.getproductusecase.GetMaxPrice
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddNewBrand
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddParentCategory
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddProduct
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddProductImage
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddSubCategory
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.DeleteProduct
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.DeleteProductImage
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetAllParentCategoryNames
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetBrandWithName
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetChildCategoriesForParent
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetChildCategoryNames
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetImage
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetLastProduct
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetParentCategoryImageUsingChild
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetParentCategoryImageUsingParentName
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetParentCategoryNameForChild
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.UpdateProduct
import com.core.usecases.productusecase.productmanagement.ProductManagementDeleteUseCases
import com.core.usecases.productusecase.productmanagement.ProductManagementGetterUseCases
import com.core.usecases.productusecase.productmanagement.ProductManagementSetterUseCases
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetUserInfoForModifiedProduct
import com.example.shoppinggroceryapp.framework.data.cart.CartDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.help.HelpDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.order.OrderDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.product.ProductDataSourceImpl
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.views.retailerviews.addeditproduct.AddEditProductViewModel
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist.CustomerRequestViewModel

class GroceryAppRetailerVMFactory(private val userDao:UserDao,
                                  private val retailerDao: RetailerDao): ViewModelProvider.Factory {

    private val cartRepository: CartRepository by lazy { CartRepository(CartDataSourceImpl(userDao))}
    private val helpRepository: HelpRepository by lazy {
        HelpRepository(
            HelpDataSourceImpl(retailerDao),
            HelpDataSourceImpl(retailerDao)
        )
    }
    private val orderRepository: OrderRepository by lazy {
        OrderRepository(
            OrderDataSourceImpl(retailerDao),
            OrderDataSourceImpl(retailerDao)
        )
    }
    private val productRepository: ProductRepository by lazy {
        ProductRepository(
            ProductDataSourceImpl(retailerDao),
            ProductDataSourceImpl(retailerDao)
        )
    }
    private val mGetBrandName: GetBrandName by lazy { GetBrandName(productRepository) }
    private val mGetMaxPrice:GetMaxPrice by lazy { GetMaxPrice(productRepository) }
    private val mGetAllParentCategoryNames: GetAllParentCategoryNames by lazy { GetAllParentCategoryNames(productRepository) }
    private val mGetParentCategoryNameForChild: GetParentCategoryNameForChild by lazy { GetParentCategoryNameForChild(productRepository) }
    private val mGetChildCategoryNames: GetChildCategoryNames by lazy { GetChildCategoryNames(productRepository) }
    private val mGetParentCategoryImageUsingChild: GetParentCategoryImageUsingChild by lazy { GetParentCategoryImageUsingChild(productRepository) }
    private val mGetParentCategoryImageUsingParentName: GetParentCategoryImageUsingParentName by lazy { GetParentCategoryImageUsingParentName(productRepository) }
    private val mGetChildCategoriesForParent: GetChildCategoriesForParent by lazy { GetChildCategoriesForParent(productRepository) }
    private val mGetImagesForProduct: GetImagesForProduct by lazy { GetImagesForProduct(productRepository) }
    private val mGetBrandWithName: GetBrandWithName by lazy { GetBrandWithName(productRepository) }
    private val mGetLastProduct: GetLastProduct by lazy {  GetLastProduct(productRepository) }
    private val mGetImage: GetImage by lazy { GetImage(productRepository) }
    private val mAddParentCategory: AddParentCategory by lazy { AddParentCategory(productRepository) }
    private val mAddSubCategory: AddSubCategory by lazy { AddSubCategory(productRepository) }
    private val mAddProduct: AddProduct by lazy { AddProduct(productRepository) }
    private val mUpdateProduct: UpdateProduct by lazy { UpdateProduct(productRepository) }
    private val mAddProductImage: AddProductImage by lazy { AddProductImage(productRepository) }
    private val mAddNewBrand: AddNewBrand by lazy { AddNewBrand(productRepository) }
    private val mDeleteProductImage: DeleteProductImage by lazy { DeleteProductImage(productRepository) }
    private val mDeleteProduct: DeleteProduct by lazy { DeleteProduct(productRepository) }
    private val mGetSpecificProductInCart: GetSpecificProductInCart by lazy { GetSpecificProductInCart(cartRepository) }
    private val mGetUserInfoForModifiedProduct:GetUserInfoForModifiedProduct by lazy { GetUserInfoForModifiedProduct(productRepository) }
    private val productManagementGetters: ProductManagementGetterUseCases by lazy { ProductManagementGetterUseCases(mGetBrandName,mGetAllParentCategoryNames, mGetParentCategoryNameForChild, mGetChildCategoryNames, mGetParentCategoryImageUsingChild, mGetParentCategoryImageUsingParentName, mGetChildCategoriesForParent, mGetImagesForProduct, mGetBrandWithName, mGetLastProduct, mGetImage,mGetMaxPrice,mGetSpecificProductInCart,mGetUserInfoForModifiedProduct) }
    private val productManagementSetters: ProductManagementSetterUseCases by lazy { ProductManagementSetterUseCases(mAddParentCategory, mAddSubCategory, mAddProduct, mUpdateProduct, mAddProductImage, mAddNewBrand) }
    private val productDeleteUseCases: ProductManagementDeleteUseCases by lazy { ProductManagementDeleteUseCases(mDeleteProductImage, mDeleteProduct) }
    private val mGetCustomerRequestWithName: GetCustomerRequestWithName by lazy { GetCustomerRequestWithName(helpRepository) }
    private val mGetOrderDetailsWithOrderId: GetOrderDetailsWithOrderId by lazy { GetOrderDetailsWithOrderId(orderRepository) }
    private val mGetProductsWithCartData: GetProductsWithCartData by lazy { GetProductsWithCartData(cartRepository) }
    private val mGetCustomerReqForSpecificUser: GetCustomerReqForSpecificUser by lazy { GetCustomerReqForSpecificUser(helpRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass){
        when{
            isAssignableFrom(AddEditProductViewModel::class.java) -> {
                AddEditProductViewModel(productManagementGetters,productManagementSetters,productDeleteUseCases)
            }
            isAssignableFrom(CustomerRequestViewModel::class.java) -> {
                CustomerRequestViewModel(mGetCustomerRequestWithName,mGetOrderDetailsWithOrderId, mGetProductsWithCartData,mGetCustomerReqForSpecificUser)
            }
            else -> {
                throw IllegalArgumentException("unknown viewmodel: ${modelClass.name}")
            }
        }
    } as T
}