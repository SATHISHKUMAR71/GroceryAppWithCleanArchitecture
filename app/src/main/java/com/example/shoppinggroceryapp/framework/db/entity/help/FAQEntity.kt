package com.example.shoppinggroceryapp.framework.db.entity.help

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FAQEntity (
    @PrimaryKey(autoGenerate = true)
    val faqId:Int,
    val question:String,
    val answer:String
)