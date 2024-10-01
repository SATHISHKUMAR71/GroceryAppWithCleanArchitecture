package com.example.shoppinggroceryapp.framework.db.entity.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistoryEntity (
    @PrimaryKey(autoGenerate = false)
    var searchText:String,
    var userId:Int
)