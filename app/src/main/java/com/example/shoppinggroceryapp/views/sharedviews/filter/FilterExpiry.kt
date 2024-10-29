package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.google.android.material.button.MaterialButton
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
        var clearAll:MutableLiveData<Boolean> =MutableLiveData()
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
//        val startDateTextInput = view.findViewById<TextView>(R.id.startDateTextView)
//        val endDateTextInput = view.findViewById<TextView>(R.id.endDateTextView)
        val clearStartDate = view.findViewById<MaterialButton>(R.id.clearStartDate)
        val clearEndDate = view.findViewById<MaterialButton>(R.id.clearEndDate)
        var endDate:String? = null
        var startDate:String? = null

        val dateManufacturePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select the From Date")
            .setTextInputFormat(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()
        val dateExpiryPicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select the To Date")
            .setTextInputFormat(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()
//        startDateTextInput.setOnFocusChangeListener { v, hasFocus ->
//            if(hasFocus){
//                dateManufacturePicker.show(parentFragmentManager,"From Date Picker")
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
//            startDateTextInput.hint = "Select From Date"
            startDateTextInputLayout.hint = "Select From Date"
            println("12345 set hint is called on line")
            clearStartDate.visibility = View.GONE
//            view.findViewById<LinearLayout>(R.id.clearStartDateLayout).visibility = View.GONE
//            clearStartDate.visibility = View.INVISIBLE
            isDataChanged.value = true
        }

        clearEndDate.setOnClickListener {
            endDateTextInput.setText("")
            endDateTextInputLayout.hint = "Select To Date"
            endDate = null
            if(isExpiry==true){
                endExpiryDate = ""
            }
            else if(isExpiry==false){
                endManufactureDate = ""
            }
            clearEndDate.visibility = View.GONE
//            view.findViewById<LinearLayout>(R.id.clearEndDateLayout).visibility = View.GONE
//            clearEndDate.visibility = View.INVISIBLE
            isDataChanged.value = true
        }
        startDateTextInput.setOnClickListener {
            dateManufacturePicker.show(parentFragmentManager,"From Date Picker")
        }
        endDateTextInput.setOnClickListener {
            dateExpiryPicker.show(parentFragmentManager,"To Date Picker")
        }
        var formatter = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        dateManufacturePicker.addOnPositiveButtonClickListener {
            startDate = formatter.format(it)

            if(endDate!=null){
                val status = DateGenerator.compareDeliveryStatus(startDate!!,endDate!!)
                if(status=="Pending"){
                    startDateTextInput.setText(DateGenerator.getDayAndMonth(formatter.format(it)))
//                    startDateTextInput.hint = "From Date"
                    startDateTextInputLayout.hint = "From Date"
                    println("12345 set hint is called on line")
                    if(isExpiry==true) {
                        startExpiryDate = startDate!!
                    }
                    else if(isExpiry == false){
                        startManufactureDate = startDate!!
                    }
                    clearStartDate.visibility = View.VISIBLE
//                    view.findViewById<LinearLayout>(R.id.clearStartDateLayout).visibility = View.VISIBLE
                    isDataChanged.value = true
                }
                else{
                    ShowShortToast.show("From Date Should Be Minimum then To Date",requireContext())
                }
            }
            else{
                startDateTextInput.setText(DateGenerator.getDayAndMonth(formatter.format(it)))
//                startDateTextInput.hint = "From Date"
                startDateTextInputLayout.hint = "From Date"
                println("12345 set hint is called on line")
                if(isExpiry==true) {
                    startExpiryDate = startDate!!
                }
                else if(isExpiry == false){
                    startManufactureDate = startDate!!
                }
                isDataChanged.value = true
                clearStartDate.visibility = View.VISIBLE
//                view.findViewById<LinearLayout>(R.id.clearStartDateLayout).visibility = View.VISIBLE
            }
        }
        dateExpiryPicker.addOnPositiveButtonClickListener {
            endDate = formatter.format(it)

            if(startDate!=null){
                val status = DateGenerator.compareDeliveryStatus(startDate!!,endDate!!)
                if(status=="Pending"){
                    endDateTextInput.setText(DateGenerator.getDayAndMonth(formatter.format(it)))
                    endDateTextInputLayout.hint = "To Date"
                    if(isExpiry==true) {
                        endExpiryDate= endDate!!
                    }
                    else if(isExpiry==false){
                        endManufactureDate = endDate!!
                    }
                    clearEndDate.visibility = View.VISIBLE
//                    view.findViewById<LinearLayout>(R.id.clearEndDateLayout).visibility = View.VISIBLE
                    isDataChanged.value = true
                }
                else{
                    ShowShortToast.show("To Date Should Be Maximum then From Date",requireContext())
                }
            }
            else{
                endDateTextInput.setText(DateGenerator.getDayAndMonth(formatter.format(it)))
                endDateTextInputLayout.hint = "To Date"
                if(isExpiry==true) {
                    endExpiryDate= endDate!!
                }
                else if(isExpiry==false){
                    endManufactureDate = endDate!!
                }
                clearEndDate.visibility = View.VISIBLE
//                view.findViewById<LinearLayout>(R.id.clearEndDateLayout).visibility = View.VISIBLE
                isDataChanged.value = true
            }
        }
        if(isExpiry==true){
            if(startExpiryDate.isNotEmpty()){
                startDateTextInput.setText(DateGenerator.getDayAndMonth(startExpiryDate))
//                startDateTextInput.hint = "From Date"
                startDateTextInputLayout.hint = "From Date"
                println("12345 set hint is called on line")
                clearStartDate.visibility = View.VISIBLE
//                view.findViewById<LinearLayout>(R.id.clearStartDateLayout).visibility = View.VISIBLE
            }
            if(endExpiryDate.isNotEmpty()){
                endDateTextInput.setText(DateGenerator.getDayAndMonth(endExpiryDate))
                endDateTextInputLayout.hint = "To Date"
//                view.findViewById<LinearLayout>(R.id.clearEndDateLayout).visibility = View.VISIBLE
                clearEndDate.visibility = View.VISIBLE
            }
        }
        else if(isExpiry==false) {
            if (startManufactureDate.isNotEmpty()) {
                startDateTextInput.setText(DateGenerator.getDayAndMonth(startManufactureDate))
//                startDateTextInput.hint = "From Date"
                startDateTextInputLayout.hint = "From Date"
                println("12345 set hint is called on line")
                clearStartDate.visibility = View.VISIBLE
//                view.findViewById<LinearLayout>(R.id.clearStartDateLayout).visibility = View.VISIBLE
            }
            if (endManufactureDate.isNotEmpty()) {
                endDateTextInput.setText(DateGenerator.getDayAndMonth(endManufactureDate))
                endDateTextInputLayout.hint = "To Date"
                clearEndDate.visibility = View.VISIBLE
//                view.findViewById<LinearLayout>(R.id.clearEndDateLayout).visibility = View.VISIBLE
            }
        }
        clearAll.observe(viewLifecycleOwner){
            startDateTextInput.setText("")
//            startDateTextInput.hint = "Select From Date"
            startDateTextInputLayout.hint = "Select From Date"
            println("12345 set hint is called on line")
            endDateTextInput.setText("")
            endDateTextInputLayout.hint = "Select To Date"
//            view.findViewById<LinearLayout>(R.id.clearEndDateLayout).visibility = View.GONE
//            view.findViewById<LinearLayout>(R.id.clearStartDateLayout).visibility = View.GONE
            clearStartDate.visibility = View.GONE
            clearEndDate.visibility = View.GONE
        }
        return view
    }
}