package com.example.shoppinggroceryapp.helpers.imagehandlers

import android.graphics.BitmapFactory
import android.widget.ImageView
import com.example.shoppinggroceryapp.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File

class SetProductImage {

    companion object {
        suspend fun setImageView(imageView: ImageView, url: String, file: File) {
            if (url.isNotEmpty()) {
                try {
                    val imageCacheData = withContext(Dispatchers.IO){
                        synchronized(MainActivity.imageCache) {
                            MainActivity.imageCache.get(url)
                        }
                    }
                    if (imageCacheData == null) {
                        val bitmap = withContext(Dispatchers.IO){
                            val imagePath = File(file, url)
                            BitmapFactory.decodeFile(imagePath.absolutePath)
                        }
                        synchronized(MainActivity.imageCache){
                            MainActivity.imageCache.put(url, bitmap)
                        }
                        withContext(Dispatchers.Main){
                            imageView.setImageBitmap(bitmap)
                        }
                    } else {
                        withContext(Dispatchers.Main){
                            imageView.setImageBitmap(MainActivity.imageCache.get(url))
                        }
                    }
                } catch (e: Exception) {
                    println("EXCEPTION: $e")
                }
            }
        }
        suspend fun loadImage(url: String,file: File){
            if (url.isNotEmpty()) {
                try {
                    val imageCacheData = withContext(Dispatchers.IO){
                        synchronized(MainActivity.imageCache) {
                            MainActivity.imageCache.get(url)
                        }
                    }
                    if (imageCacheData == null) {
                        val bitmap = withContext(Dispatchers.IO){
                            val imagePath = File(file, url)
                            BitmapFactory.decodeFile(imagePath.absolutePath)
                        }
                        synchronized(MainActivity.imageCache){
                            MainActivity.imageCache.put(url, bitmap)
                        }
                    }
                } catch (e: Exception) {
                    println("EXCEPTION: $e")
                }
            }
        }
    }
}