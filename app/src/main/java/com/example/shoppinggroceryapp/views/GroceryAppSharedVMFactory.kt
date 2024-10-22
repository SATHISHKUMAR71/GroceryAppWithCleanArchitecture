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
import com.core.usecases.addressusecase.GetSpecificAddress
import com.core.usecases.cartusecase.setcartusecase.AddCartForUser
import com.core.usecases.cartusecase.setcartusecase.AddProductInCart
import com.core.usecases.cartusecase.getcartusecase.GetCartForUser
import com.core.usecases.cartusecase.getcartusecase.GetCartItems
import com.core.usecases.cartusecase.getcartusecase.GetDeletedProductsWithCarId
import com.core.usecases.cartusecase.getcartusecase.GetProductsByCartId
import com.core.usecases.cartusecase.getcartusecase.GetProductsWithCartData
import com.core.usecases.cartusecase.getcartusecase.GetSpecificProductInCart
import com.core.usecases.cartusecase.setcartusecase.RemoveProductInCart
import com.core.usecases.cartusecase.setcartusecase.UpdateCartItems
import com.core.usecases.filterusecases.GetAllBrands
import com.core.usecases.orderusecase.getordersusecase.GetOrderForUser
import com.core.usecases.subscriptionusecase.getsubscriptionusecase.GetOrderForUserDailySubscription
import com.core.usecases.subscriptionusecase.getsubscriptionusecase.GetOrderForUserMonthlySubscription
import com.core.usecases.subscriptionusecase.getsubscriptionusecase.GetOrderForUserWeeklySubscription
import com.core.usecases.subscriptionusecase.getsubscriptionusecase.GetOrdersForUserNoSubscription
import com.core.usecases.orderusecase.getordersusecase.GetPurchasedProducts
import com.core.usecases.orderusecase.updateorderusecase.UpdateOrderDetails
import com.core.usecases.productusecase.getproductusecase.GetAllProducts
import com.core.usecases.productusecase.getproductusecase.GetBrandName
import com.core.usecases.productusecase.getproductusecase.GetImagesForProduct
import com.core.usecases.productusecase.getproductusecase.GetProductByName
import com.core.usecases.productusecase.getproductusecase.GetProductsByCategory
import com.core.usecases.orderusecase.getordersusecase.GetAllOrders
import com.core.usecases.orderusecase.getordersusecase.GetDailyOrders
import com.core.usecases.orderusecase.getordersusecase.GetDailySubscriptionOrderWithTimeSlot
import com.core.usecases.orderusecase.getordersusecase.GetMonthlyOrders
import com.core.usecases.orderusecase.getordersusecase.GetMonthlySubscriptionWithTimeSlot
import com.core.usecases.orderusecase.getordersusecase.GetNormalOrder
import com.core.usecases.orderusecase.getordersusecase.GetWeeklyOrders
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddDeletedProductInDb
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.DeleteProduct
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetProductInRecentList
import com.core.usecases.userusecase.AddNewUser
import com.core.usecases.productusecase.setproductusecase.AddProductInRecentList
import com.core.usecases.searchusecase.AddSearchQueryInDb
import com.core.usecases.searchusecase.GetSearchList
import com.core.usecases.userusecase.GetUser
import com.core.usecases.userusecase.GetUserByInputData
import com.core.usecases.searchusecase.PerformCategorySearch
import com.core.usecases.searchusecase.PerformProductSearch
import com.core.usecases.userusecase.UpdateExistingUser
import com.core.usecases.orderusecase.getordersusecase.GetOrderedTimeSlot
import com.core.usecases.orderusecase.getordersusecase.GetSpecificDailyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificMonthlyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificOrder
import com.core.usecases.orderusecase.getordersusecase.GetSpecificWeeklyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificWeeklyOrderWithTimeSlot
import com.core.usecases.productusecase.getproductusecase.AddProductToWishList
import com.core.usecases.productusecase.getproductusecase.GetAvailableProductsInOrder
import com.core.usecases.productusecase.getproductusecase.GetProductsById
import com.core.usecases.productusecase.getproductusecase.GetSpecificProductWishlist
import com.core.usecases.productusecase.getproductusecase.GetWishListProducts
import com.core.usecases.productusecase.getproductusecase.GetWishLists
import com.core.usecases.productusecase.getproductusecase.RemoveFromWishList
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetUserInfoForModifiedProduct
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.UpdateProduct
import com.core.usecases.productusecase.setproductusecase.RemoveFromRecentlyViewedProducts
import com.core.usecases.productusecase.setproductusecase.UpdateAvailableProducts
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromDailySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromMonthlySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromWeeklySubscription
import com.core.usecases.userusecase.GetLastlyOrderedDateForProduct
import com.example.shoppinggroceryapp.framework.data.address.AddressDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.authentication.AuthenticationDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.cart.CartDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.help.HelpDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.order.OrderDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.product.ProductDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.search.SearchDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.subscription.SubscriptionDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.user.UserDataSourceImpl
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.login.LoginViewModel
import com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.signup.SignUpViewModel
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterViewModel
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail.OrderDetailViewModel
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListViewModel
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail.ProductDetailViewModel
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.sharedviews.profileviews.EditProfileViewModel
import com.example.shoppinggroceryapp.views.sharedviews.search.SearchViewModel

class GroceryAppSharedVMFactory (private val retailerDao:RetailerDao,
                                 private val userDao:UserDao)
    : ViewModelProvider.Factory {

    private val userRepository:UserRepository by lazy {UserRepository(UserDataSourceImpl(userDao))}
    private val authenticationRepository :AuthenticationRepository by lazy { AuthenticationRepository(AuthenticationDataSourceImpl(userDao))}
    private val cartRepository: CartRepository by lazy {CartRepository(CartDataSourceImpl(userDao))}
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
    private val searchRepository: SearchRepository by lazy {SearchRepository(SearchDataSourceImpl(userDao))}
    private val subscriptionRepository: SubscriptionRepository by lazy {
        SubscriptionRepository(
            SubscriptionDataSourceImpl(userDao),
            SubscriptionDataSourceImpl(userDao),
            SubscriptionDataSourceImpl(userDao)
        )
    }
    private val addressRepository: AddressRepository by lazy {AddressRepository(AddressDataSourceImpl(userDao))}

    private val mGetSearchList: GetSearchList by lazy { GetSearchList(searchRepository) }
    private val mPerformProductSearch: PerformProductSearch by lazy { PerformProductSearch(searchRepository) }
    private val mPerformCategorySearch: PerformCategorySearch by lazy { PerformCategorySearch(searchRepository) }
    private val mAddSearchQueryInDb: AddSearchQueryInDb by lazy { AddSearchQueryInDb(searchRepository) }
    private val mGetBrandName: GetBrandName by lazy { GetBrandName(productRepository) }
    private val mGetImagesForProduct: GetImagesForProduct by lazy { GetImagesForProduct(productRepository) }
    private val mDeleteProduct: DeleteProduct by lazy { DeleteProduct(productRepository) }
    private val mUpdateExistingUser: UpdateExistingUser by lazy { UpdateExistingUser(userRepository) }
    private val mGetUserByInputData: GetUserByInputData by lazy { GetUserByInputData(authenticationRepository) }
    private val mGetPurchasedProducts: GetPurchasedProducts by lazy { GetPurchasedProducts(orderRepository) }
    private val mGetProductsByCartId: GetProductsByCartId by lazy { GetProductsByCartId(cartRepository) }
    private val mGetProductsWithCartData: GetProductsWithCartData by lazy { GetProductsWithCartData(cartRepository) }
    private val mGetUser: GetUser by lazy { GetUser(authenticationRepository) }
    private val mGetCartForUser: GetCartForUser by lazy { GetCartForUser(cartRepository) }
    private val addCartForUser: AddCartForUser by lazy { AddCartForUser(cartRepository) }
    private val mAddNewUser: AddNewUser by lazy { AddNewUser(userRepository) }
    private val mUpdateOrderDetails: UpdateOrderDetails by lazy { UpdateOrderDetails(orderRepository) }
    private val mGetProductById : GetProductsById by lazy { GetProductsById(productRepository) }
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
    private val mGetMonthlySubscriptionWithTimeSlot: GetMonthlySubscriptionWithTimeSlot by lazy { GetMonthlySubscriptionWithTimeSlot(subscriptionRepository) }
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
    private val mGetSpecificWeeklyOrderWithTimeSlot:GetSpecificWeeklyOrderWithTimeSlot by lazy { GetSpecificWeeklyOrderWithTimeSlot(subscriptionRepository) }
    private val mGetDailySubscriptionOrderWithTimeSlot:GetDailySubscriptionOrderWithTimeSlot by lazy { GetDailySubscriptionOrderWithTimeSlot(subscriptionRepository) }
    private val mGetCartItems: GetCartItems by lazy { GetCartItems(cartRepository) }
    private val mGetAllBrands:GetAllBrands by lazy { GetAllBrands(productRepository) }
    private val mGetLastlyOrderedDateForProduct:GetLastlyOrderedDateForProduct by lazy { GetLastlyOrderedDateForProduct(productRepository) }
    private val mRemoveFromRecentlyViewedProducts: RemoveFromRecentlyViewedProducts by lazy { RemoveFromRecentlyViewedProducts(productRepository) }
    private val mAddProductToWishList:AddProductToWishList by lazy { AddProductToWishList(productRepository) }
    private val mRemoveFromWishList:RemoveFromWishList by lazy { RemoveFromWishList(productRepository) }
    private val mUpdateAvailableProducts: UpdateAvailableProducts by lazy { UpdateAvailableProducts(productRepository) }
    private val mGetWishList:GetWishLists by lazy { GetWishLists(productRepository) }
    private val mGetWishListProducts: GetWishListProducts by lazy { GetWishListProducts(productRepository) }
    private val mGetSpecificProductWishlist: GetSpecificProductWishlist by lazy{ GetSpecificProductWishlist(productRepository) }
    private val mGetUserInfoForModifiedProduct: GetUserInfoForModifiedProduct by lazy { GetUserInfoForModifiedProduct(productRepository) }
    private val mUpdateProduct: UpdateProduct by lazy { UpdateProduct(productRepository) }
    private val mGetAvailableProductsInOrder: GetAvailableProductsInOrder by lazy { GetAvailableProductsInOrder(productRepository) }
    private val mGetSpecificOrder: GetSpecificOrder by lazy { GetSpecificOrder(productRepository) }
    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass){
        when{
            isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(mGetSearchList,mPerformProductSearch, mPerformCategorySearch, mAddSearchQueryInDb)
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
                OrderDetailViewModel(mUpdateOrderDetails, mGetSpecificAddress, mGetSpecificDailyOrderWithOrderId, mGetSpecificMonthlyOrderWithOrderId, mGetSpecificWeeklyOrderWithOrderId, mRemoveOrderFromMonthlySubscription, mRemoveOrderFromDailySubscription, mRemoveOrderFromWeeklySubscription,mGetProductById, mUpdateProduct,mGetOrderedTimeSlot)
            }
            isAssignableFrom(OrderListViewModel::class.java)->{
                OrderListViewModel(mGetOrderForUser,mUpdateOrderDetails, mGetOrderForUserMonthlySubscription, mGetOrderForUserDailySubscription, mGetOrderForUserWeeklySubscription, mGetOrdersForUserNoSubscription, mGetProductsWithCartData, mGetDeletedProductsWithCarId, mGetWeeklyOrders, mGetMonthlyOrders, mGetNormalOrder, mGetDailyOrders, mGetAllOrders,mGetDailySubscriptionOrderWithTimeSlot, mGetMonthlySubscriptionWithTimeSlot, mGetSpecificWeeklyOrderWithTimeSlot, mGetOrderedTimeSlot)
            }
            isAssignableFrom(ProductDetailViewModel::class.java)->{
                ProductDetailViewModel(mDeleteProduct, mGetBrandName, mGetProductsByCartId, mGetProductInRecentList, mAddProductInRecentList, mGetSpecificProductInCart, mGetProductsByCategory,mGetUserInfoForModifiedProduct, mAddProductInCart, mUpdateCartItems,mGetAvailableProductsInOrder, mRemoveProductInCart, mUpdateOrderDetails,mGetSpecificOrder,mGetImagesForProduct, mAddProductToWishList, mRemoveFromWishList, mGetSpecificProductWishlist, mAddDeletedProductInDb, mRemoveFromRecentlyViewedProducts)
            }
            isAssignableFrom(ProductListViewModel::class.java)->{
                ProductListViewModel(mGetProductsByCategory, mGetProductByName, mGetAllProducts,mAddProductInCart, mUpdateAvailableProducts,mGetSpecificProductInCart, mGetBrandName, mRemoveProductInCart, mUpdateCartItems,mGetImagesForProduct,mGetWishListProducts,mAddProductToWishList,mGetWishList,mRemoveFromWishList,mGetLastlyOrderedDateForProduct, mGetCartItems)
            }
            isAssignableFrom(FilterViewModel::class.java) -> {
                FilterViewModel(mGetAllBrands)
            }
            else -> {
                throw IllegalArgumentException("unknown viewmodel: ${modelClass.name}")
            }
        }
    } as T
}