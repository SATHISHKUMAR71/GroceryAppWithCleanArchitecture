package com.example.shoppinggroceryapp.views.sharedviews.profileviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.core.data.repository.AuthenticationRepository
import com.core.data.repository.CartRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.UserRepository
import com.core.usecases.customerusecase.cart.GetProductsByCartId
import com.core.usecases.customerusecase.orders.GetPurchasedProducts
import com.core.usecases.userusecase.GetUserByInputData
import com.core.usecases.userusecase.UpdateExistingUser

class EditProfileViewModelFactory(private val userRepository: UserRepository,
                                  private val orderRepository: OrderRepository,
                                  private val cartRepository: CartRepository,
                                  private val authenticationRepository: AuthenticationRepository):ViewModelProvider.Factory {
    private val mUpdateExistingUser: UpdateExistingUser by lazy { UpdateExistingUser(userRepository) }
    private val mGetPurchasedProducts: GetPurchasedProducts by lazy { GetPurchasedProducts(orderRepository) }
    private val mGetProductsByCartId: GetProductsByCartId by lazy { GetProductsByCartId(cartRepository) }

    private val mGetUserByInputData: GetUserByInputData by lazy { GetUserByInputData(authenticationRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditProfileViewModel(mUpdateExistingUser,mGetUserByInputData, mGetPurchasedProducts, mGetProductsByCartId) as T
    }
}