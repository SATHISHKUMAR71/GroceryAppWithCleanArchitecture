package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.ScrollIndicators
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider

class FilterPrice : Fragment() {


    companion object{
        var priceStartFrom = 0f
        var clearAll:MutableLiveData<Boolean> =MutableLiveData()
        var isPriceDataChanged:MutableLiveData<Boolean> = MutableLiveData()
        var MAX_PRICE_VALUE = 200f
        var priceEndTo = MAX_PRICE_VALUE
    }
    private lateinit var rangeSlider: RangeSlider
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filter_price, container, false)
        rangeSlider = view.findViewById<RangeSlider>(R.id.range_slider)
        val priceFrom = view.findViewById<TextView>(R.id.priceFrom)
        val priceTo = view.findViewById<TextView>(R.id.priceTo)
//        rangeSlider.setCustomThumbDrawable(R.drawable.custome_thumb_drawable)
        rangeSlider.setMinSeparationValue(50F)
        rangeSlider.setValues(0f, MAX_PRICE_VALUE)
        rangeSlider.stepSize = 10F
        rangeSlider.valueFrom = 0f
        rangeSlider.valueTo = MAX_PRICE_VALUE
//        rangeSlider.setLabelFormatter {
//            "₹ ${it.toInt()}"
//            null
//        }
        rangeSlider.labelBehavior = LabelFormatter.LABEL_GONE
        rangeSlider.trackHeight = 10
        rangeSlider.thumbHeight=50
        rangeSlider.thumbWidth=50

//        priceFrom.text = priceStartFrom.toString()
//        priceTo.text = priceEndTo.toString()+" "
        println("RANGE SLIDER VALUES before start value: $priceStartFrom end value: $priceEndTo org from value:${ rangeSlider.values[0]}  org to value: ${rangeSlider.values[1]}")
        rangeSlider.values[1] = priceEndTo
        rangeSlider.values[0] = priceStartFrom
        println("RANGE SLIDER VALUES middle start value: $priceStartFrom end value: $priceEndTo org from value:${ rangeSlider.values[0]}  org to value: ${rangeSlider.values[1]}")
//        rangeSlider.haloRadius = 0
//        rangeSlider.thumbTrackGapSize=0
        rangeSlider.addOnChangeListener { slider, value, fromUser ->
            priceTo.text = rangeSlider.values[1].toInt().toString()+" "
            priceFrom.text = rangeSlider.values[0].toInt().toString()
//            if(rangeSlider.values[1].toInt()==2010){
//                priceTo.text = "2000+"
//            }
            priceStartFrom =  rangeSlider.values[0].toInt().toFloat()
            priceEndTo = rangeSlider.values[1].toInt().toFloat()
            isPriceDataChanged.value = true
            println("RANGE SLIDER VALUES ${rangeSlider.values} ${value.toInt()} price: ${slider.values[1].toInt() - slider.values[0].toInt()}")
        }
        priceTo.text = rangeSlider.values[1].toInt().toString()+" "
        priceFrom.text = rangeSlider.values[0].toInt().toString()
//        if(rangeSlider.values[1].toInt()==2010){
//            priceTo.text = "2000+"
//        }
        println("RANGE SLIDER VALUES after start value: $priceStartFrom end value: $priceEndTo org from value:${ rangeSlider.values[0]}  org to value: ${rangeSlider.values[1]}")
        clearAll.observe(viewLifecycleOwner){
            priceEndTo = MAX_PRICE_VALUE
            priceStartFrom = 0f
            rangeSlider.setValues(0f, MAX_PRICE_VALUE)
        }
        view.findViewById<MaterialButton>(R.id.resetPriceButton).setOnClickListener {
            priceEndTo = MAX_PRICE_VALUE
            priceStartFrom = 0f
            rangeSlider.setValues(0f, MAX_PRICE_VALUE)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        println("ON RESUME RANGE SLIDER VALUES before start value: $priceStartFrom end value: $priceEndTo org from value:${ rangeSlider.values[0]}  org to value: ${rangeSlider.values[1]}")
//        rangeSlider.valueTo = priceEndTo
        rangeSlider.setValues(priceStartFrom, priceEndTo)
//        rangeSlider.valueFrom = priceStartFrom
        println("ON RESUME RANGE SLIDER VALUES middle start value: $priceStartFrom end value: $priceEndTo org from value:${ rangeSlider.values[0]}  org to value: ${rangeSlider.values[1]}")
    }
}