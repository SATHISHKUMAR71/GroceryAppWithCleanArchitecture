package com.example.shoppinggroceryapp.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.core.data.repository.CustomerRepository
import com.core.data.repository.RetailerRepository
import com.core.data.repository.UserRepository
import com.core.usecases.customerusecase.address.AddNewAddress
import com.core.usecases.customerusecase.address.GetAllAddress
import com.core.usecases.customerusecase.address.GetSpecificAddress
import com.core.usecases.customerusecase.address.UpdateAddress
import com.core.usecases.customerusecase.cart.AddCartForUser
import com.core.usecases.customerusecase.cart.AddProductInCart
import com.core.usecases.customerusecase.cart.GetCartForUser
import com.core.usecases.customerusecase.cart.GetCartItems
import com.core.usecases.customerusecase.cart.GetDeletedProductsWithCarId
import com.core.usecases.customerusecase.cart.GetProductsByCartId
import com.core.usecases.customerusecase.cart.GetProductsWithCartData
import com.core.usecases.customerusecase.cart.GetSpecificProductInCart
import com.core.usecases.customerusecase.cart.RemoveProductInCart
import com.core.usecases.customerusecase.cart.UpdateCart
import com.core.usecases.customerusecase.cart.UpdateCartItems
import com.core.usecases.customerusecase.help.AddCustomerRequest
import com.core.usecases.customerusecase.orders.AddDailySubscription
import com.core.usecases.customerusecase.orders.AddMonthlySubscription
import com.core.usecases.customerusecase.orders.AddOrder
import com.core.usecases.customerusecase.orders.AddTimeSlot
import com.core.usecases.customerusecase.orders.AddWeeklySubscription
import com.core.usecases.customerusecase.orders.GetOrderDetailsWithOrderId
import com.core.usecases.customerusecase.orders.GetOrderForUser
import com.core.usecases.customerusecase.orders.GetOrderForUserDailySubscription
import com.core.usecases.customerusecase.orders.GetOrderForUserMonthlySubscription
import com.core.usecases.customerusecase.orders.GetOrderForUserWeeklySubscription
import com.core.usecases.customerusecase.orders.GetOrderWithProductsByOrderId
import com.core.usecases.customerusecase.orders.GetOrdersForUserNoSubscription
import com.core.usecases.customerusecase.orders.GetPurchasedProducts
import com.core.usecases.customerusecase.orders.UpdateOrderDetails
import com.core.usecases.customerusecase.orders.UpdateTimeSlot
import com.core.usecases.customerusecase.products.GetAllProducts
import com.core.usecases.customerusecase.products.GetBrandName
import com.core.usecases.customerusecase.products.GetImagesForProduct
import com.core.usecases.customerusecase.products.GetOfferedProducts
import com.core.usecases.customerusecase.products.GetProductByName
import com.core.usecases.customerusecase.products.GetProductsByCategory
import com.core.usecases.customerusecase.products.GetProductsById
import com.core.usecases.customerusecase.products.GetRecentlyViewedProducts
import com.core.usecases.retailerusecase.customer.GetCustomerRequestWithName
import com.core.usecases.retailerusecase.orders.GetAllOrders
import com.core.usecases.retailerusecase.orders.GetDailyOrders
import com.core.usecases.retailerusecase.orders.GetMonthlyOrders
import com.core.usecases.retailerusecase.orders.GetNormalOrder
import com.core.usecases.retailerusecase.orders.GetWeeklyOrders
import com.core.usecases.retailerusecase.products.AddDeletedProductInDb
import com.core.usecases.retailerusecase.products.AddNewBrand
import com.core.usecases.retailerusecase.products.AddParentCategory
import com.core.usecases.retailerusecase.products.AddProduct
import com.core.usecases.retailerusecase.products.AddProductImage
import com.core.usecases.retailerusecase.products.AddSubCategory
import com.core.usecases.retailerusecase.products.DeleteProduct
import com.core.usecases.retailerusecase.products.DeleteProductImage
import com.core.usecases.retailerusecase.products.GetAllParentCategoryNames
import com.core.usecases.retailerusecase.products.GetBrandWithName
import com.core.usecases.retailerusecase.products.GetChildCategoriesForParent
import com.core.usecases.retailerusecase.products.GetChildCategoryNames
import com.core.usecases.retailerusecase.products.GetImage
import com.core.usecases.retailerusecase.products.GetLastProduct
import com.core.usecases.retailerusecase.products.GetParentCategoryImageUsingChild
import com.core.usecases.retailerusecase.products.GetParentCategoryImageUsingParentName
import com.core.usecases.retailerusecase.products.GetParentCategoryNameForChild
import com.core.usecases.retailerusecase.products.GetProductInRecentList
import com.core.usecases.retailerusecase.products.UpdateProduct
import com.core.usecases.retailerusecase.products.productmanagement.ProductManagementDeleteUseCases
import com.core.usecases.retailerusecase.products.productmanagement.ProductManagementGetterUseCases
import com.core.usecases.retailerusecase.products.productmanagement.ProductManagementSetterUseCases
import com.core.usecases.userusecase.AddNewUser
import com.core.usecases.userusecase.AddProductInRecentList
import com.core.usecases.userusecase.AddSearchQueryInDb
import com.core.usecases.userusecase.GetChildNames
import com.core.usecases.userusecase.GetParentCategories
import com.core.usecases.userusecase.GetSearchList
import com.core.usecases.userusecase.GetUser
import com.core.usecases.userusecase.GetUserByInputData
import com.core.usecases.userusecase.PerformCategorySearch
import com.core.usecases.userusecase.PerformProductSearch
import com.core.usecases.userusecase.UpdateExistingUser
import com.core.usecases.userusecase.orders.GetOrderedTimeSlot
import com.core.usecases.userusecase.orders.GetSpecificDailyOrderWithOrderId
import com.core.usecases.userusecase.orders.GetSpecificMonthlyOrderWithOrderId
import com.core.usecases.userusecase.orders.GetSpecificWeeklyOrderWithOrderId
import com.core.usecases.userusecase.orders.RemoveOrderFromDailySubscription
import com.core.usecases.userusecase.orders.RemoveOrderFromMonthlySubscription
import com.core.usecases.userusecase.orders.RemoveOrderFromWeeklySubscription
import com.example.shoppinggroceryapp.views.retailerviews.addeditproduct.AddEditProductViewModel
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist.CustomerRequestViewModel
import com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.login.LoginViewModel
import com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.signup.SignUpViewModel
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail.OrderDetailViewModel
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListViewModel
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail.ProductDetailViewModel
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.sharedviews.profileviews.EditProfileViewModel
import com.example.shoppinggroceryapp.views.sharedviews.search.SearchViewModel
import com.example.shoppinggroceryapp.views.userviews.addressview.getaddress.GetAddressViewModel
import com.example.shoppinggroceryapp.views.userviews.addressview.getaddress.GetNewAddress
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.SavedAddressList
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.SavedAddressViewModel
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartViewModel
import com.example.shoppinggroceryapp.views.userviews.category.CategoryViewModel
import com.example.shoppinggroceryapp.views.userviews.help.HelpViewModel
import com.example.shoppinggroceryapp.views.userviews.home.HomeViewModel
import com.example.shoppinggroceryapp.views.userviews.offer.OfferViewModel
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersuccess.OrderSuccessViewModel
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary.OrderSummaryViewModel

class GroceryAppViewModelFactory(private val userRepository: UserRepository,private val retailerRepository: RetailerRepository,private val customerRepository: CustomerRepository):ViewModelProvider.Factory {

    private val mGetSearchList: GetSearchList by lazy { GetSearchList(userRepository) }
    private val mPerformProductSearch: PerformProductSearch by lazy { PerformProductSearch(userRepository) }
    private val mPerformCategorySearch: PerformCategorySearch by lazy { PerformCategorySearch(userRepository) }
    private val mAddSearchQueryInDb: AddSearchQueryInDb by lazy { AddSearchQueryInDb(userRepository) }
    private val mGetBrandName:GetBrandName by lazy { GetBrandName(customerRepository) }
    private val mGetAllParentCategoryNames:GetAllParentCategoryNames by lazy { GetAllParentCategoryNames(retailerRepository) }
    private val mGetParentCategoryNameForChild:GetParentCategoryNameForChild by lazy { GetParentCategoryNameForChild(retailerRepository) }
    private val mGetChildCategoryNames:GetChildCategoryNames by lazy { GetChildCategoryNames(retailerRepository) }
    private val mGetParentCategoryImageUsingChild:GetParentCategoryImageUsingChild by lazy { GetParentCategoryImageUsingChild(retailerRepository) }
    private val mGetParentCategoryImageUsingParentName:GetParentCategoryImageUsingParentName by lazy { GetParentCategoryImageUsingParentName(retailerRepository) }
    private val mGetChildCategoriesForParent:GetChildCategoriesForParent by lazy { GetChildCategoriesForParent(retailerRepository) }
    private val mGetImagesForProduct:GetImagesForProduct by lazy { GetImagesForProduct(customerRepository) }
    private val mGetBrandWithName:GetBrandWithName by lazy { GetBrandWithName(retailerRepository) }
    private val mGetLastProduct:GetLastProduct by lazy {  GetLastProduct(retailerRepository) }
    private val mGetImage:GetImage by lazy { GetImage(retailerRepository) }
    private val mAddParentCategory:AddParentCategory by lazy { AddParentCategory(retailerRepository) }
    private val mAddSubCategory:AddSubCategory by lazy { AddSubCategory(retailerRepository) }
    private val mAddProduct:AddProduct by lazy { AddProduct(retailerRepository) }
    private val mUpdateProduct:UpdateProduct by lazy { UpdateProduct(retailerRepository) }
    private val mAddProductImage:AddProductImage by lazy { AddProductImage(retailerRepository) }
    private val mAddNewBrand:AddNewBrand by lazy { AddNewBrand(retailerRepository) }
    private val mDeleteProductImage:DeleteProductImage by lazy { DeleteProductImage(retailerRepository) }
    private val mDeleteProduct:DeleteProduct by lazy { DeleteProduct(retailerRepository) }
    private val mUpdateExistingUser: UpdateExistingUser by lazy { UpdateExistingUser(customerRepository) }
    private val mGetUserByInputData: GetUserByInputData by lazy { GetUserByInputData(customerRepository) }
    private val mGetPurchasedProducts: GetPurchasedProducts by lazy { GetPurchasedProducts(customerRepository) }
    private val mGetProductsByCartId: GetProductsByCartId by lazy { GetProductsByCartId(customerRepository) }
    private val productManagementGetters:ProductManagementGetterUseCases by lazy { ProductManagementGetterUseCases(mGetBrandName,mGetAllParentCategoryNames, mGetParentCategoryNameForChild, mGetChildCategoryNames, mGetParentCategoryImageUsingChild, mGetParentCategoryImageUsingParentName, mGetChildCategoriesForParent, mGetImagesForProduct, mGetBrandWithName, mGetLastProduct, mGetImage) }
    private val productManagementSetters:ProductManagementSetterUseCases by lazy { ProductManagementSetterUseCases(mAddParentCategory, mAddSubCategory, mAddProduct, mUpdateProduct, mAddProductImage, mAddNewBrand) }
    private val productDeleteUseCases:ProductManagementDeleteUseCases by lazy { ProductManagementDeleteUseCases(mDeleteProductImage, mDeleteProduct) }
    private val mGetCustomerRequestWithName:GetCustomerRequestWithName by lazy { GetCustomerRequestWithName(retailerRepository) }
    private val mGetOrderDetailsWithOrderId: GetOrderDetailsWithOrderId by lazy { GetOrderDetailsWithOrderId(customerRepository) }
    private val mGetProductsWithCartData: GetProductsWithCartData by lazy { GetProductsWithCartData(customerRepository) }
    private val mGetUser: GetUser by lazy { GetUser(customerRepository) }
    private val mGetCartForUser: GetCartForUser by lazy { GetCartForUser(customerRepository) }
    private val addCartForUser: AddCartForUser by lazy { AddCartForUser(customerRepository) }
    private val mAddNewUser:AddNewUser by lazy { AddNewUser(customerRepository) }
    private val mUpdateOrderDetails: UpdateOrderDetails by lazy { UpdateOrderDetails(customerRepository) }
    private val mGetSpecificAddress: GetSpecificAddress by lazy { GetSpecificAddress(customerRepository) }
    private val mGetSpecificDailyOrderWithOrderId: GetSpecificDailyOrderWithOrderId by lazy { GetSpecificDailyOrderWithOrderId(userRepository) }
    private val mGetSpecificMonthlyOrderWithOrderId: GetSpecificMonthlyOrderWithOrderId by lazy { GetSpecificMonthlyOrderWithOrderId(userRepository) }
    private val mGetSpecificWeeklyOrderWithOrderId: GetSpecificWeeklyOrderWithOrderId by lazy { GetSpecificWeeklyOrderWithOrderId(userRepository) }
    private val mRemoveOrderFromMonthlySubscription: RemoveOrderFromMonthlySubscription by lazy { RemoveOrderFromMonthlySubscription(userRepository) }
    private val mRemoveOrderFromDailySubscription: RemoveOrderFromDailySubscription by lazy { RemoveOrderFromDailySubscription(userRepository) }
    private val mRemoveOrderFromWeeklySubscription: RemoveOrderFromWeeklySubscription by lazy { RemoveOrderFromWeeklySubscription(userRepository) }
    private val mGetOrderedTimeSlot: GetOrderedTimeSlot by lazy { GetOrderedTimeSlot(userRepository) }
    private val mGetOrderForUser: GetOrderForUser by lazy { GetOrderForUser(customerRepository) }
    private val mGetOrderForUserMonthlySubscription: GetOrderForUserMonthlySubscription by lazy { GetOrderForUserMonthlySubscription(customerRepository) }
    private val mGetOrderForUserDailySubscription: GetOrderForUserDailySubscription by lazy { GetOrderForUserDailySubscription(customerRepository) }
    private val mGetOrderForUserWeeklySubscription: GetOrderForUserWeeklySubscription by lazy { GetOrderForUserWeeklySubscription(customerRepository) }
    private val mGetOrdersForUserNoSubscription: GetOrdersForUserNoSubscription by lazy { GetOrdersForUserNoSubscription(customerRepository) }
    private val mGetDeletedProductsWithCarId: GetDeletedProductsWithCarId by lazy { GetDeletedProductsWithCarId(customerRepository) }
    private val mGetWeeklyOrders: GetWeeklyOrders by lazy { GetWeeklyOrders(retailerRepository) }
    private val mGetMonthlyOrders: GetMonthlyOrders by lazy { GetMonthlyOrders(retailerRepository) }
    private val mGetNormalOrder: GetNormalOrder by lazy { GetNormalOrder(retailerRepository) }
    private val mGetDailyOrders: GetDailyOrders by lazy { GetDailyOrders(retailerRepository) }
    private val mGetAllOrders: GetAllOrders by lazy { GetAllOrders(retailerRepository) }
    private val mAddProductInRecentList: AddProductInRecentList by lazy { AddProductInRecentList(userRepository) }
    private val mGetProductInRecentList: GetProductInRecentList by lazy { GetProductInRecentList(retailerRepository) }
    private val mGetSpecificProductInCart: GetSpecificProductInCart by lazy { GetSpecificProductInCart(customerRepository) }
    private val mGetProductsByCategory: GetProductsByCategory by lazy { GetProductsByCategory(customerRepository) }
    private val mAddProductInCart: AddProductInCart by lazy { AddProductInCart(customerRepository) }
    private val mUpdateCartItems: UpdateCartItems by lazy { UpdateCartItems(customerRepository) }
    private val mRemoveProductInCart: RemoveProductInCart by lazy { RemoveProductInCart(customerRepository) }
    private val mAddDeletedProductInDb: AddDeletedProductInDb by lazy { AddDeletedProductInDb(retailerRepository) }
    private val mGetProductByName: GetProductByName by lazy { GetProductByName(customerRepository) }
    private val mGetAllProducts: GetAllProducts by lazy { GetAllProducts(customerRepository) }
    private val mGetCartItems: GetCartItems by lazy { GetCartItems(customerRepository) }
    private val mAddNewAddress: AddNewAddress by lazy { AddNewAddress(customerRepository) }
    private val mUpdateAddress: UpdateAddress by lazy { UpdateAddress(customerRepository) }
    private val mGetAddressList: GetAllAddress by lazy { GetAllAddress(customerRepository) }
    private val mGetParentCategories: GetParentCategories by lazy { GetParentCategories(userRepository) }
    private val mGetChildNames: GetChildNames by lazy { GetChildNames(userRepository) }
    private val mAddCustomerRequest: AddCustomerRequest by lazy { AddCustomerRequest(customerRepository) }
    private val mGetRecentlyViewedProducts: GetRecentlyViewedProducts by lazy { GetRecentlyViewedProducts(customerRepository) }
    private val mGetProductsById: GetProductsById by lazy { GetProductsById(customerRepository) }
    private val mGetOfferedProducts: GetOfferedProducts by lazy { GetOfferedProducts(customerRepository) }
    private val mAddOrder: AddOrder by lazy { AddOrder(customerRepository) }
    private val mGetOrderWithProductsByOrderId: GetOrderWithProductsByOrderId by lazy { GetOrderWithProductsByOrderId(customerRepository) }
    private val mAddMonthlySubscription: AddMonthlySubscription by lazy { AddMonthlySubscription(customerRepository) }
    private val mAddWeeklySubscription: AddWeeklySubscription by lazy { AddWeeklySubscription(customerRepository) }
    private val mAddDailySubscription: AddDailySubscription by lazy { AddDailySubscription(customerRepository) }
    private val mAddTimeSlot: AddTimeSlot by lazy { AddTimeSlot(customerRepository) }
    private val mUpdateCart: UpdateCart by lazy { UpdateCart(customerRepository) }
    private val mAddCartForUser:AddCartForUser by lazy { AddCartForUser(customerRepository) }
    private val mUpdateTimeSlot: UpdateTimeSlot by lazy { UpdateTimeSlot(userRepository) }


    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass){
        when{
            isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(mGetSearchList,mPerformProductSearch, mPerformCategorySearch, mAddSearchQueryInDb)
            }
            isAssignableFrom(AddEditProductViewModel::class.java) -> {
                AddEditProductViewModel(productManagementGetters,productManagementSetters,productDeleteUseCases)
            }
            isAssignableFrom(CustomerRequestViewModel::class.java) -> {
                CustomerRequestViewModel(mGetCustomerRequestWithName,mGetOrderDetailsWithOrderId, mGetProductsWithCartData)
            }
            isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(mUpdateExistingUser,mGetUserByInputData, mGetPurchasedProducts, mGetProductsByCartId)
            }
            isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(mGetUser, mGetUserByInputData, mGetCartForUser, addCartForUser)
            }
            isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(mGetUserByInputData,mAddNewUser)
            }
            isAssignableFrom(OrderDetailViewModel::class.java) -> {
                OrderDetailViewModel(mUpdateOrderDetails, mGetSpecificAddress, mGetSpecificDailyOrderWithOrderId, mGetSpecificMonthlyOrderWithOrderId, mGetSpecificWeeklyOrderWithOrderId, mRemoveOrderFromMonthlySubscription, mRemoveOrderFromDailySubscription, mRemoveOrderFromWeeklySubscription, mGetOrderedTimeSlot)
            }
            isAssignableFrom(OrderListViewModel::class.java)->{
                OrderListViewModel(mGetOrderForUser, mGetOrderForUserMonthlySubscription, mGetOrderForUserDailySubscription, mGetOrderForUserWeeklySubscription, mGetOrdersForUserNoSubscription, mGetProductsWithCartData, mGetDeletedProductsWithCarId, mGetWeeklyOrders, mGetMonthlyOrders, mGetNormalOrder, mGetDailyOrders, mGetAllOrders)
            }
            isAssignableFrom(ProductDetailViewModel::class.java)->{
                ProductDetailViewModel(mDeleteProduct, mGetBrandName, mGetProductsByCartId, mGetProductInRecentList, mAddProductInRecentList, mGetSpecificProductInCart, mGetProductsByCategory, mAddProductInCart, mUpdateCartItems, mRemoveProductInCart, mGetImagesForProduct, mAddDeletedProductInDb)
            }
            isAssignableFrom(ProductListViewModel::class.java)->{
                ProductListViewModel(mGetProductsByCategory, mGetProductByName, mGetAllProducts,mAddProductInCart, mGetSpecificProductInCart, mGetBrandName, mRemoveProductInCart, mUpdateCartItems, mGetCartItems)
            }
            isAssignableFrom(GetAddressViewModel::class.java)->{
                GetAddressViewModel(mAddNewAddress, mUpdateAddress)
            }
            isAssignableFrom(SavedAddressViewModel::class.java)->{
                SavedAddressViewModel(mGetAddressList)
            }
            isAssignableFrom(CartViewModel::class.java)->{
                CartViewModel(mGetProductsByCartId,mGetCartItems,mGetAddressList)
            }
            isAssignableFrom(CategoryViewModel::class.java)->{
                CategoryViewModel(mGetParentCategories, mGetChildNames)
            }
            isAssignableFrom(HelpViewModel::class.java)->{
                HelpViewModel(mGetProductsWithCartData, mAddCustomerRequest)
            }
            isAssignableFrom(HomeViewModel::class.java)->{
                HomeViewModel(mGetRecentlyViewedProducts,mGetProductsById)
            }
            isAssignableFrom(OfferViewModel::class.java)->{
                OfferViewModel(mGetOfferedProducts)
            }
            isAssignableFrom(OrderSuccessViewModel::class.java)->{
                OrderSuccessViewModel(mAddOrder, mGetOrderWithProductsByOrderId, mAddMonthlySubscription, mAddWeeklySubscription, mAddDailySubscription, mAddTimeSlot, mUpdateCart, mAddCartForUser, mGetCartForUser)
            }
            isAssignableFrom(OrderSummaryViewModel::class.java)->{
                OrderSummaryViewModel(mGetProductsWithCartData, mUpdateOrderDetails, mUpdateTimeSlot, mAddMonthlySubscription, mAddWeeklySubscription, mAddDailySubscription, mGetSpecificMonthlyOrderWithOrderId, mGetSpecificWeeklyOrderWithOrderId, mGetSpecificDailyOrderWithOrderId, mRemoveOrderFromDailySubscription, mRemoveOrderFromWeeklySubscription, mRemoveOrderFromMonthlySubscription)
            }
            else -> {
                throw IllegalArgumentException("unknown viewmodel: ${modelClass.name}")
            }
        }
    } as T
}