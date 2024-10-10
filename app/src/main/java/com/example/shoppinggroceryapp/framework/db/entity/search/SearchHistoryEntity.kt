package com.example.shoppinggroceryapp.framework.db.entity.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistoryEntity (
    @PrimaryKey(autoGenerate = false)
    var searchText:String,
    var userId:Int
)


//@Entity(primaryKeys = ["searchText", "userId"])
//data class SearchHistoryEntity (
//    var searchText:String,
//    var userId:Int
//)