package com.example.shoppinggroceryapp.framework.data

import com.core.domain.order.OrderDetails
import com.core.domain.products.Product
import com.core.domain.user.User
import com.example.shoppinggroceryapp.framework.db.entity.order.OrderDetailsEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity
import com.example.shoppinggroceryapp.framework.db.entity.user.UserEntity

class ConvertorHelper {
    fun convertUserToUserEntity(user: User): UserEntity {
        return UserEntity(user.userId,user.userImage,user.userFirstName,user.userLastName,user.userEmail,user.userPhone,
            user.userPassword,user.dateOfBirth,user.isRetailer)
    }
    fun convertUserEntityToUser(user: UserEntity): User {
        return User(user.userId,user.userImage,user.userFirstName,user.userLastName,user.userEmail,user.userPhone,
            user.userPassword,user.dateOfBirth,user.isRetailer)
    }

    fun convertOrderDetailsToOrderDetailsEntity(orderDetails: OrderDetails): OrderDetailsEntity {
        return OrderDetailsEntity(orderDetails.orderId,orderDetails.cartId,orderDetails.addressId,orderDetails.paymentMode,
            orderDetails.deliveryFrequency,orderDetails.paymentStatus,orderDetails.deliveryStatus,orderDetails.deliveryDate,orderDetails.orderedDate)
    }
    fun convertOrderEntityToOrderDetails(orderDetailsEntity: OrderDetailsEntity): OrderDetails {
        return OrderDetails(orderDetailsEntity.orderId,orderDetailsEntity.cartId,orderDetailsEntity.addressId,orderDetailsEntity.paymentMode,
            orderDetailsEntity.deliveryFrequency,orderDetailsEntity.paymentStatus,orderDetailsEntity.deliveryStatus,orderDetailsEntity.deliveryDate,orderDetailsEntity.orderedDate)
    }

    fun convertOrderDetailsEntityToOrderDetails(orderDetails:List<OrderDetailsEntity>):List<OrderDetails>{
        return orderDetails.map { OrderDetails(it.orderId,it.cartId,it.addressId,it.paymentMode,it.deliveryFrequency
            ,it.paymentStatus,it.deliveryStatus,it.deliveryDate,it.orderedDate) }
    }

    fun convertProductEntityToProduct(product: ProductEntity): Product {
        return Product(product.productId,product.brandId,product.categoryName,product.productName,product.productDescription,
            product.price,product.offer,product.productQuantity,product.mainImage,product.isVeg,product.manufactureDate,
            product.expiryDate,product.availableItems)
    }
    fun convertProductToProductEntity(product: Product): ProductEntity {
        return ProductEntity(product.productId,product.brandId,product.categoryName,product.productName,product.productDescription,
            product.price,product.offer,product.productQuantity,product.mainImage,product.isVeg,product.manufactureDate,
            product.expiryDate,product.availableItems)
    }
}