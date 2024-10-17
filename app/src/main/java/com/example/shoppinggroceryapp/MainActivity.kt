package com.example.shoppinggroceryapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.LruCache
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.login.LoginFragment
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase.Companion.getAppDatabase
import com.example.shoppinggroceryapp.framework.db.entity.order.CartMappingEntity
import com.example.shoppinggroceryapp.views.initialview.SetInitialDataForUser
import com.example.shoppinggroceryapp.views.sharedviews.profileviews.AccountFragment
import com.example.shoppinggroceryapp.views.sharedviews.profileviews.EditProfileViewModel


class MainActivity : AppCompatActivity() {
    companion object{
        val handler = Handler(Looper.getMainLooper())
        var userFirstName = ""
        var userLastName = ""
        var userId = "-1"
        var userEmail = ""
        var imageCache = LruCache<String,Bitmap>((Runtime.getRuntime().maxMemory()/4).toInt())
        var userPhone = ""
        var selectedAddress = -1
        var cartId = 0
        var userImage = ""
        var isRetailer = false
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread{
            getAppDatabase(baseContext).getUserDao().initDB()
        }.start()
        val pref = getSharedPreferences("freshCart", Context.MODE_PRIVATE)
        SetInitialDataForUser().invoke(pref)
        val isSigned = pref.getBoolean("isSigned",false)
        if(isSigned){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentBody, InitialFragment())
                .commit()
        }
        else{
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentBody, LoginFragment())
                .commit()
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100)
        }
        val db2 = getAppDatabase(baseContext).getUserDao()
        if(isSigned) {
            assignCart(db2)
        }

    }
    private fun assignCart(db2: UserDao){
        Thread {
            val cart: CartMappingEntity? = db2.getCartForUser(userId.toInt())
            if (cart == null) {
                db2.addCartForUser(CartMappingEntity(0, userId = userId.toInt(), "available"))
                val newCart = db2.getCartForUser(userId.toInt())
                cartId = newCart?.cartId?:-1
            } else {
                cartId = cart.cartId
            }
        }.start()
        Thread{
            println("767676 ${db2.getProductsByCartId(MainActivity.cartId)} product list: ${db2.getProductById(8)}")
        }.start()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        AccountFragment().restartApp()
    }
}