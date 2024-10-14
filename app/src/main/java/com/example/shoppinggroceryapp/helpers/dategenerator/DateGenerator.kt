package com.example.shoppinggroceryapp.helpers.dategenerator

import android.annotation.SuppressLint
import android.icu.util.LocaleData
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class DateGenerator {

    companion object {

        private val months = listOf(
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        )
        @SuppressLint("NewApi")
        fun getCurrentDate(): String {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return currentDate.format(formatter)
        }

        @SuppressLint("NewApi")
        fun getDeliveryDate(): String {
            val currentDate = LocalDate.now()
            val nextDay = currentDate.plusDays(1)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return nextDay.format(formatter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentTime():Int{
            return LocalTime.now().hour
        }

        @SuppressLint("NewApi")
        fun getNextMonth(): String {
            val currentDate = LocalDate.now()
            val nextDay = currentDate.plusMonths(1)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return nextDay.format(formatter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentDay():String{
            val currentDate = LocalDate.now()
            val currentDay = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            return currentDay
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentDayOfMonth():String{
            val currentDate = LocalDate.now()
            val currentDay = currentDate.dayOfMonth
            return currentDay.toString()
        }

        @SuppressLint("NewApi")
        fun compareDeliveryStatus(currentDate:String, deliveryDate:String):String{
            val currentDateList = currentDate.split("-")
            val deliveryDateList = deliveryDate.split("-")
            val current = LocalDate.of(currentDateList[0].toInt(),currentDateList[1].toInt(),currentDateList[2].toInt())
            val delivery = LocalDate.of(deliveryDateList[0].toInt(),deliveryDateList[1].toInt(),deliveryDateList[2].toInt())
            if(current.isBefore(delivery)){
                return "Pending"
            }
            else if(current.isAfter(delivery)){
                return "Delivered"
            }
            else{
                return "Out For Delivery"
            }
        }

        fun getDayAndMonth(date:String):String{

            val currentDateList = date.split("-")
            val month = currentDateList[1].toInt()
            val day = currentDateList[2].toInt()
            return "${months[month-1]} $day, ${currentDateList[0]}"
        }

        fun getDayAndMonthForDay(deliveryDay:String):String{

            var date = getCurrentDate().substring(0,8)
            if(deliveryDay.toInt() < getCurrentDate().substring(8).toInt()){
                date = getNextMonth().substring(0,8)
            }

            date += deliveryDay
            val currentDateList = date.split("-")
            val month = currentDateList[1].toInt()
            val day = currentDateList[2].toInt()
            return "${months[month-1]} $day, ${currentDateList[0]}"
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getNextDayForSpecificMonth(dayOfMonth: String):String{
            var updatedDate = ""
            var date = getCurrentDate()
            updatedDate += date.substring(0,8)
            updatedDate += dayOfMonth
            val splitDate =updatedDate.split("-")
            println("212112 NEXT splitted date: $splitDate")
            val deliveryDateDateFormat = LocalDate.of(splitDate[0].toInt(),splitDate[1].toInt(),splitDate[2].toInt())
            println("212112 NEXT value in date Generator ${deliveryDateDateFormat.plusMonths(1)} ")
//            return deliveryDateDateFormat.plusMonths(1).toString()
            val nextMonth = deliveryDateDateFormat.plusMonths(1).toString().split("-")
            return "${months[nextMonth[1].toInt()-1]} ${nextMonth[2]}, ${nextMonth[0]}"
        }
    }

}