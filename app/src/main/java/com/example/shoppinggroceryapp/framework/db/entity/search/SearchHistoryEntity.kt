package com.example.shoppinggroceryapp.framework.db.entity.search

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(primaryKeys = ["searchText", "userId"])
data class SearchHistoryEntity (
    var searchText:String,
    var userId:Int
)