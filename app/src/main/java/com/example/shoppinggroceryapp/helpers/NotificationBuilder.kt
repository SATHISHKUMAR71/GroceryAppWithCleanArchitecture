package com.example.shoppinggroceryapp.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.core.domain.products.Product
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R

class NotificationBuilder(var context: Context) {
    private var notificationManager:NotificationManager? = null
    private val NOTIFICATION_CHANNEL_ID = "Stocks are Less"
    private val PENDING_INTENT_REQ_CODE = 200

    @RequiresApi(Build.VERSION_CODES.O)

    fun createNotificationChannel(){
        val channelName = context.resources.getString(R.string.channel_name)
        val description = context.resources.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,channelName,importance).apply {
            setDescription(description)
        }
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.createNotificationChannel(channel)
        println("isNewlyCreated notification channel created")
    }

    fun showNotification(productName:Product,contentTitle:String,contentMessage:String,bigContentMessage:String){
        println("675743 notification called: $productName")
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("isEditProduct",true)
            putExtra("productName",productName.productName)
            putExtra("productId",productName.productId)
            putExtra("brandId",productName.brandId)
            putExtra("categoryName",productName.categoryName)
            putExtra("productName",productName.productName)
            putExtra("productDescription",productName.productDescription)
            putExtra("price",productName.price)
            putExtra("offer",productName.offer)
            putExtra("productQuantity",productName.productQuantity)
            putExtra("mainImage",productName.mainImage)
            putExtra("isVeg",productName.isVeg)
            putExtra("manufactureDate",productName.manufactureDate)
            putExtra("expiryDate",productName.expiryDate)
            putExtra("availableItems",productName.availableItems)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context,SystemClock.uptimeMillis().toInt(),intent,
            PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.shopping_cart_24px)
            .setContentTitle(contentTitle)
            .setContentText(contentMessage)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigContentMessage))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager?.notify(SystemClock.uptimeMillis().toInt(),notificationBuilder.build())
    }
}