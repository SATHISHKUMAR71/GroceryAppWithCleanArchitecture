package com.example.shoppinggroceryapp.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.core.data.repository.AddressRepository
import com.core.data.repository.AuthenticationRepository
import com.core.data.repository.CartRepository
import com.core.data.repository.HelpRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.data.repository.SearchRepository
import com.core.data.repository.SubscriptionRepository
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
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.sharedviews.profileviews.EditProfileViewModel
import com.example.shoppinggroceryapp.views.sharedviews.search.SearchViewModel
import com.example.shoppinggroceryapp.views.userviews.addressview.getaddress.GetAddressViewModel
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.SavedAddressViewModel
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartViewModel
import com.example.shoppinggroceryapp.views.userviews.category.CategoryViewModel
import com.example.shoppinggroceryapp.views.userviews.help.HelpViewModel
import com.example.shoppinggroceryapp.views.userviews.home.HomeViewModel
import com.example.shoppinggroceryapp.views.userviews.offer.OfferViewModel
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersuccess.OrderSuccessViewModel
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary.OrderSummaryViewModel

class GroceryAppViewModelFactory(private val userRepository: UserRepository,
                                  private val authenticationRepository: AuthenticationRepository,
                                  private val cartRepository: CartRepository,
                                  private val helpRepository: HelpRepository,
                                  private val orderRepository: OrderRepository,
                                  private val productRepository: ProductRepository,
                                  private val searchRepository: SearchRepository,
                                  private val subscriptionRepository: SubscriptionRepository,
                                  private val addressRepository: AddressRepository): ViewModelProvider.Factory {

    private val mGetSearchList: GetSearchList by lazy { GetSearchList(searchRepository) }
    private val mPerformProductSearch: PerformProductSearch by lazy { PerformProductSearch(searchRepository) }
    private val mPerformCategorySearch: PerformCategorySearch by lazy { PerformCategorySearch(searchRepository) }
    private val mAddSearchQueryInDb: AddSearchQueryInDb by lazy { AddSearchQueryInDb(searchRepository) }
    private val mGetBrandName: GetBrandName by lazy { GetBrandName(productRepository) }
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
    private val mUpdateExistingUser: UpdateExistingUser by lazy { UpdateExistingUser(userRepository) }
    private val mGetUserByInputData: GetUserByInputData by lazy { GetUserByInputData(authenticationRepository) }
    private val mGetPurchasedProducts: GetPurchasedProducts by lazy { GetPurchasedProducts(orderRepository) }
    private val mGetProductsByCartId: GetProductsByCartId by lazy { GetProductsByCartId(cartRepository) }
    private val productManagementGetters: ProductManagementGetterUseCases by lazy { ProductManagementGetterUseCases(mGetBrandName,mGetAllParentCategoryNames, mGetParentCategoryNameForChild, mGetChildCategoryNames, mGetParentCategoryImageUsingChild, mGetParentCategoryImageUsingParentName, mGetChildCategoriesForParent, mGetImagesForProduct, mGetBrandWithName, mGetLastProduct, mGetImage) }
    private val productManagementSetters: ProductManagementSetterUseCases by lazy { ProductManagementSetterUseCases(mAddParentCategory, mAddSubCategory, mAddProduct, mUpdateProduct, mAddProductImage, mAddNewBrand) }
    private val productDeleteUseCases: ProductManagementDeleteUseCases by lazy { ProductManagementDeleteUseCases(mDeleteProductImage, mDeleteProduct) }
    private val mGetCustomerRequestWithName: GetCustomerRequestWithName by lazy { GetCustomerRequestWithName(helpRepository) }
    private val mGetOrderDetailsWithOrderId: GetOrderDetailsWithOrderId by lazy { GetOrderDetailsWithOrderId(orderRepository) }
    private val mGetProductsWithCartData: GetProductsWithCartData by lazy { GetProductsWithCartData(cartRepository) }
    private val mGetUser: GetUser by lazy { GetUser(authenticationRepository) }
    private val mGetCartForUser: GetCartForUser by lazy { GetCartForUser(cartRepository) }
    private val addCartForUser: AddCartForUser by lazy { AddCartForUser(cartRepository) }
    private val mAddNewUser: AddNewUser by lazy { AddNewUser(userRepository) }
    private val mUpdateOrderDetails: UpdateOrderDetails by lazy { UpdateOrderDetails(orderRepository) }
    private val mGetSpecificAddress: GetSpecificAddress by lazy { GetSpecificAddress(addressRepository) }
    private val mGetSpecificDailyOrderWithOrderId: GetSpecificDailyOrderWithOrderId by lazy { GetSpecificDailyOrderWithOrderId(subscriptionRepository) }
    private val mGetSpecificMonthlyOrderWithOrderId: GetSpecificMonthlyOrderWithOrderId by lazy { GetSpecificMonthlyOrderWithOrderId(subscriptionRepository) }
    private val mGetSpecificWeeklyOrderWithOrderId: GetSpecificWeeklyOrderWithOrderId by lazy { GetSpecificWeeklyOrderWithOrderId(subscriptionRepository) }
    private val mRemoveOrderFromMonthlySubscription: RemoveOrderFromMonthlySubscription by lazy { RemoveOrderFromMonthlySubscription(subscriptionRepository) }
    private val mRemoveOrderFromDailySubscription: RemoveOrderFromDailySubscription by lazy { RemoveOrderFromDailySubscription(subscriptionRepository) }
    private val mRemoveOrderFromWeeklySubscription: RemoveOrderFromWeeklySubscription by lazy { RemoveOrderFromWeeklySubscription(subscriptionRepository) }
    private val mGetOrderedTimeSlot: GetOrderedTimeSlot by lazy { GetOrderedTimeSlot(subscriptionRepository) }
    private val mGetOrderForUser: GetOrderForUser by lazy { GetOrderForUser(orderRepository) }
    private val mGetOrderForUserMonthlySubscription: GetOrderForUserMonthlySubscription by lazy { GetOrderForUserMonthlySubscription(orderRepository) }
    private val mGetOrderForUserDailySubscription: GetOrderForUserDailySubscription by lazy { GetOrderForUserDailySubscription(orderRepository) }
    private val mGetOrderForUserWeeklySubscription: GetOrderForUserWeeklySubscription by lazy { GetOrderForUserWeeklySubscription(orderRepository) }
    private val mGetOrdersForUserNoSubscription: GetOrdersForUserNoSubscription by lazy { GetOrdersForUserNoSubscription(orderRepository) }
    private val mGetDeletedProductsWithCarId: GetDeletedProductsWithCarId by lazy { GetDeletedProductsWithCarId(cartRepository) }
    private val mGetWeeklyOrders: GetWeeklyOrders by lazy { GetWeeklyOrders(orderRepository) }
    private val mGetMonthlyOrders: GetMonthlyOrders by lazy { GetMonthlyOrders(orderRepository) }
    private val mGetNormalOrder: GetNormalOrder by lazy { GetNormalOrder(orderRepository) }
    private val mGetDailyOrders: GetDailyOrders by lazy { GetDailyOrders(orderRepository) }
    private val mGetAllOrders: GetAllOrders by lazy { GetAllOrders(orderRepository) }
    private val mAddProductInRecentList: AddProductInRecentList by lazy { AddProductInRecentList(productRepository) }
    private val mGetProductInRecentList: GetProductInRecentList by lazy { GetProductInRecentList(productRepository) }
    private val mGetSpecificProductInCart: GetSpecificProductInCart by lazy { GetSpecificProductInCart(cartRepository) }
    private val mGetProductsByCategory: GetProductsByCategory by lazy { GetProductsByCategory(productRepository) }
    private val mAddProductInCart: AddProductInCart by lazy { AddProductInCart(cartRepository) }
    private val mUpdateCartItems: UpdateCartItems by lazy { UpdateCartItems(cartRepository) }
    private val mRemoveProductInCart: RemoveProductInCart by lazy { RemoveProductInCart(cartRepository) }
    private val mAddDeletedProductInDb: AddDeletedProductInDb by lazy { AddDeletedProductInDb(productRepository) }
    private val mGetProductByName: GetProductByName by lazy { GetProductByName(productRepository) }
    private val mGetAllProducts: GetAllProducts by lazy { GetAllProducts(productRepository) }
    private val mGetCartItems: GetCartItems by lazy { GetCartItems(cartRepository) }
    private val mAddNewAddress: AddNewAddress by lazy { AddNewAddress(addressRepository) }
    private val mUpdateAddress: UpdateAddress by lazy { UpdateAddress(addressRepository) }
    private val mGetAddressList: GetAllAddress by lazy { GetAllAddress(addressRepository) }
    private val mGetParentCategories: GetParentCategories by lazy { GetParentCategories(productRepository) }
    private val mGetChildNames: GetChildNames by lazy { GetChildNames(productRepository) }
    private val mAddCustomerRequest: AddCustomerRequest by lazy { AddCustomerRequest(helpRepository) }
    private val mGetRecentlyViewedProducts: GetRecentlyViewedProducts by lazy { GetRecentlyViewedProducts(productRepository) }
    private val mGetProductsById: GetProductsById by lazy { GetProductsById(productRepository) }
    private val mGetOfferedProducts: GetOfferedProducts by lazy { GetOfferedProducts(productRepository) }
    private val mAddOrder: AddOrder by lazy { AddOrder(orderRepository) }
    private val mGetOrderWithProductsByOrderId: GetOrderWithProductsByOrderId by lazy { GetOrderWithProductsByOrderId(orderRepository) }
    private val mAddMonthlySubscription: AddMonthlySubscription by lazy { AddMonthlySubscription(subscriptionRepository) }
    private val mAddWeeklySubscription: AddWeeklySubscription by lazy { AddWeeklySubscription(subscriptionRepository) }
    private val mAddDailySubscription: AddDailySubscription by lazy { AddDailySubscription(subscriptionRepository) }
    private val mAddTimeSlot: AddTimeSlot by lazy { AddTimeSlot(subscriptionRepository) }
    private val mUpdateCart: UpdateCart by lazy { UpdateCart(cartRepository) }
    private val mAddCartForUser: AddCartForUser by lazy { AddCartForUser(cartRepository) }
    private val mUpdateTimeSlot: UpdateTimeSlot by lazy { UpdateTimeSlot(subscriptionRepository) }


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