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
import com.core.usecases.userusecase.GetParentAndChildCategories
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
import com.example.shoppinggroceryapp.framework.data.address.AddressDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.cart.CartDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.help.HelpDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.order.OrderDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.product.ProductDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.search.SearchDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.subscription.SubscriptionDataSourceImpl
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
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

class GroceryAppUserVMFactory (private val userDao: UserDao,
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
    private val searchRepository: SearchRepository by lazy {  SearchRepository(SearchDataSourceImpl(userDao))}
    private val subscriptionRepository: SubscriptionRepository by lazy {
        SubscriptionRepository(
            SubscriptionDataSourceImpl(userDao),
            SubscriptionDataSourceImpl(userDao),
            SubscriptionDataSourceImpl(userDao)
        )
    }
    private val addressRepository: AddressRepository by lazy { AddressRepository(AddressDataSourceImpl(userDao))}

    private val mGetProductsByCartId: GetProductsByCartId by lazy { GetProductsByCartId(cartRepository) }
    private val mGetProductsWithCartData: GetProductsWithCartData by lazy { GetProductsWithCartData(cartRepository) }
    private val mGetCartForUser: GetCartForUser by lazy { GetCartForUser(cartRepository) }
    private val mUpdateOrderDetails: UpdateOrderDetails by lazy { UpdateOrderDetails(orderRepository) }
    private val mGetSpecificDailyOrderWithOrderId: GetSpecificDailyOrderWithOrderId by lazy { GetSpecificDailyOrderWithOrderId(subscriptionRepository) }
    private val mGetSpecificMonthlyOrderWithOrderId: GetSpecificMonthlyOrderWithOrderId by lazy { GetSpecificMonthlyOrderWithOrderId(subscriptionRepository) }
    private val mGetSpecificWeeklyOrderWithOrderId: GetSpecificWeeklyOrderWithOrderId by lazy { GetSpecificWeeklyOrderWithOrderId(subscriptionRepository) }
    private val mRemoveOrderFromMonthlySubscription: RemoveOrderFromMonthlySubscription by lazy { RemoveOrderFromMonthlySubscription(subscriptionRepository) }
    private val mRemoveOrderFromDailySubscription: RemoveOrderFromDailySubscription by lazy { RemoveOrderFromDailySubscription(subscriptionRepository) }
    private val mRemoveOrderFromWeeklySubscription: RemoveOrderFromWeeklySubscription by lazy { RemoveOrderFromWeeklySubscription(subscriptionRepository) }
    private val mGetCartItems: GetCartItems by lazy { GetCartItems(cartRepository) }
    private val mAddNewAddress: AddNewAddress by lazy { AddNewAddress(addressRepository) }
    private val mUpdateAddress: UpdateAddress by lazy { UpdateAddress(addressRepository) }
    private val mGetAddressList: GetAllAddress by lazy { GetAllAddress(addressRepository) }
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
    private val mGetParentAndChild:GetParentAndChildCategories by lazy { GetParentAndChildCategories(productRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass){
        when{
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
                CategoryViewModel(mGetParentAndChild)
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