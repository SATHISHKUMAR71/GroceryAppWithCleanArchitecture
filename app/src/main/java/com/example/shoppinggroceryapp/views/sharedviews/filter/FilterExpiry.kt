package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.MutableLiveData
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Locale

class FilterExpiry : Fragment() {

    companion object{
        var startManufactureDate = ""
        var endManufactureDate = ""
        var startExpiryDate = ""
        var endExpiryDate= ""
        var isExpiry:Boolean? = null
        var isDataChanged:MutableLiveData<Boolean> = MutableLiveData()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filter_expiry, container, false)
        isExpiry = arguments?.getBoolean("isExpiry")
        val startDateTextInput = view.findViewById<TextInputEditText>(R.id.startDateTextInput)
        val startDateTextInputLayout = view.findViewById<TextInputLayout>(R.id.startDateLayout)
        val endDateTextInput = view.findViewById<TextInputEditText>(R.id.endDateTextInput)
        val endDateTextInputLayout = view.findViewById<TextInputLayout>(R.id.endDateLayout)
        val clearStartDate = view.findViewById<ImageButton>(R.id.clearStartDate)
        val clearEndDate = view.findViewById<ImageButton>(R.id.clearEndDate)
        var endDate:String? = null
        var startDate:String? = null

        val dateManufacturePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select the Start Date")
            .setTextInputFormat(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()
        val dateExpiryPicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select the End Date")
            .setTextInputFormat(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()
//        startDateTextInput.setOnFocusChangeListener { v, hasFocus ->
//            if(hasFocus){
//                dateManufacturePicker.show(parentFragmentManager,"Start Date Picker")
//            }
//        }
        clearStartDate.setOnClickListener {
            startDate = null
            if(isExpiry==true) {
                startExpiryDate = ""
            }
            else if(isExpiry==false){
                startManufactureDate = ""
            }
            startDateTextInput.setText("")
            clearStartDate.visibility = View.INVISIBLE
            isDataChanged.value = true
        }

        clearEndDate.setOnClickListener {
            endDateTextInput.setText("")
            endDate = null
            if(isExpiry==true){
                endExpiryDate = ""
            }
            else if(isExpiry==false){
                endManufactureDate = ""
            }
            clearEndDate.visibility = View.INVISIBLE
            isDataChanged.value = true
        }
        startDateTextInput.setOnClickListener {
            dateManufacturePicker.show(parentFragmentManager,"Start Date Picker")
        }
        endDateTextInput.setOnClickListener {
            dateExpiryPicker.show(parentFragmentManager,"End Date Picker")
        }
        var formatter = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        dateManufacturePicker.addOnPositiveButtonClickListener {
            startDate = formatter.format(it)
            if(isExpiry==true) {
                startExpiryDate = startDate!!
            }
            else if(isExpiry == false){
                startManufactureDate = startDate!!
            }
            if(endDate!=null){
                val status = DateGenerator.compareDeliveryStatus(startDate!!,endDate!!)
                if(status=="Pending"){
                    startDateTextInput.setText(DateGenerator.getDayAndMonth(formatter.format(it)))
                    clearStartDate.visibility = View.VISIBLE
                    isDataChanged.value = true
                }
                else{
                    ShowShortToast.show("Start Date Should Be Minimum then End Date",requireContext())
                }
            }
            else{
                startDateTextInput.setText(DateGenerator.getDayAndMonth(formatter.format(it)))
                isDataChanged.value = true
                clearStartDate.visibility = View.VISIBLE
            }
        }
        dateExpiryPicker.addOnPositiveButtonClickListener {
            endDate = formatter.format(it)
            if(isExpiry==true) {
                endExpiryDate= endDate!!
            }
            else if(isExpiry==false){
                endManufactureDate = endDate!!
            }
            if(startDate!=null){
                val status = DateGenerator.compareDeliveryStatus(startDate!!,endDate!!)
                if(status=="Pending"){
                    endDateTextInput.setText(DateGenerator.getDayAndMonth(formatter.format(it)))
                    clearEndDate.visibility = View.VISIBLE
                    isDataChanged.value = true
                }
                else{
                    ShowShortToast.show("End Date Should Be Maximum then Start Date",requireContext())
                }
            }
            else{
                endDateTextInput.setText(DateGenerator.getDayAndMonth(formatter.format(it)))
                clearEndDate.visibility = View.VISIBLE
                isDataChanged.value = true
            }
        }
        if(isExpiry==true){
            if(startExpiryDate.isNotEmpty()){
                startDateTextInput.setText(DateGenerator.getDayAndMonth(startExpiryDate))
                clearStartDate.visibility = View.VISIBLE
            }
            if(endExpiryDate.isNotEmpty()){
                endDateTextInput.setText(DateGenerator.getDayAndMonth(endExpiryDate))
                clearEndDate.visibility = View.VISIBLE
            }
        }
        else if(isExpiry==false) {
            if (startManufactureDate.isNotEmpty()) {
                startDateTextInput.setText(DateGenerator.getDayAndMonth(startManufactureDate))
                clearStartDate.visibility = View.VISIBLE
            }
            if (endManufactureDate.isNotEmpty()) {
                endDateTextInput.setText(DateGenerator.getDayAndMonth(endManufactureDate))
                clearEndDate.visibility = View.VISIBLE
            }
        }

        return view
    }
}