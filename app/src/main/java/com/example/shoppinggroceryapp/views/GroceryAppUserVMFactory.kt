package com.example.shoppinggroceryapp.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.core.data.repository.AddressRepository
import com.core.data.repository.CartRepository
import com.core.data.repository.HelpRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.data.repository.SearchRepository
import com.core.data.repository.SubscriptionRepository
import com.core.usecases.addressusecase.AddNewAddress
import com.core.usecases.addressusecase.GetAllAddress
import com.core.usecases.addressusecase.UpdateAddress
import com.core.usecases.cartusecase.setcartusecase.AddCartForUser
import com.core.usecases.cartusecase.getcartusecase.GetCartForUser
import com.core.usecases.cartusecase.getcartusecase.GetCartItems
import com.core.usecases.cartusecase.getcartusecase.GetProductsByCartId
import com.core.usecases.cartusecase.getcartusecase.GetProductsWithCartData
import com.core.usecases.cartusecase.setcartusecase.UpdateCart
import com.core.usecases.helpusecase.AddCustomerRequest
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddDailySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddMonthlySubscription
import com.core.usecases.orderusecase.updateorderusecase.AddOrder
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddTimeSlot
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.AddWeeklySubscription
import com.core.usecases.orderusecase.getordersusecase.GetOrderWithProductsByOrderId
import com.core.usecases.orderusecase.updateorderusecase.UpdateOrderDetails
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.UpdateTimeSlot
import com.core.usecases.productusecase.getproductusecase.GetOfferedProducts
import com.core.usecases.productusecase.getproductusecase.GetProductsById
import com.core.usecases.productusecase.getproductusecase.GetRecentlyViewedProducts
import com.core.usecases.productusecase.getproductusecase.GetParentAndChildCategories
import com.core.usecases.orderusecase.getordersusecase.GetSpecificDailyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificMonthlyOrderWithOrderId
import com.core.usecases.orderusecase.getordersusecase.GetSpecificWeeklyOrderWithOrderId
import com.core.usecases.productusecase.setproductusecase.UpdateAvailableProducts
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromDailySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromMonthlySubscription
import com.core.usecases.subscriptionusecase.setsubscriptionusecase.RemoveOrderFromWeeklySubscription
import com.example.shoppinggroceryapp.framework.data.address.AddressDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.cart.CartDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.help.HelpDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.order.OrderDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.product.ProductDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.search.SearchDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.subscription.SubscriptionDataSourceImpl
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
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
    private val mGetParentAndChild: GetParentAndChildCategories by lazy { GetParentAndChildCategories(productRepository) }
    private val mUpdateAvailableProducts: UpdateAvailableProducts by lazy { UpdateAvailableProducts(productRepository) }

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
                OrderSuccessViewModel(mAddOrder, mGetOrderWithProductsByOrderId, mAddMonthlySubscription, mAddWeeklySubscription,mUpdateAvailableProducts,mAddDailySubscription,mGetProductsById, mAddTimeSlot, mUpdateCart, mAddCartForUser, mGetCartForUser,mGetCartItems)
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