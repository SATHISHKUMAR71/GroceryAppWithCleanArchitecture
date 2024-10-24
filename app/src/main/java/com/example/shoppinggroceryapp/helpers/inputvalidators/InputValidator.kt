package com.example.shoppinggroceryapp.helpers.inputvalidators

class InputValidator {

    companion object{
        fun checkEmail(email:String):Boolean{
            val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-za-z]{2,}$")
            return regex.matches(email)
        }

        fun checkPhone(phone:String):Boolean{
            val regex = Regex("^(91|\\+91)?\\s?[6-9][0-9]{9}")
            return regex.matches(phone)
        }
    }
}