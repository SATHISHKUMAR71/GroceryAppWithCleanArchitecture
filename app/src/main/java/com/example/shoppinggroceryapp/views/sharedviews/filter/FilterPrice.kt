package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.ScrollIndicators
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider

class FilterPrice : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filter_price, container, false)
        val rangeSlider = view.findViewById<RangeSlider>(R.id.range_slider)
        val priceFrom = view.findViewById<TextView>(R.id.priceFrom)
        val priceTo = view.findViewById<TextView>(R.id.priceTo)
//        rangeSlider.setCustomThumbDrawable(R.drawable.custome_thumb_drawable)
        rangeSlider.setMinSeparationValue(400F)
        rangeSlider.stepSize = 10F



//        rangeSlider.setLabelFormatter {
//            "₹ ${it.toInt()}"
//            null
//        }
        rangeSlider.labelBehavior = LabelFormatter.LABEL_GONE
        rangeSlider.trackHeight = 10
        rangeSlider.thumbHeight=50
        rangeSlider.thumbWidth=50
//        rangeSlider.haloRadius = 0
//        rangeSlider.thumbTrackGapSize=0
        rangeSlider.addOnChangeListener { slider, value, fromUser ->
            priceTo.text = rangeSlider.values[1].toInt().toString()+" "
            priceFrom.text = rangeSlider.values[0].toInt().toString()
            if(rangeSlider.values[1].toInt()==2010){
                priceTo.text = "2000+"
            }
            println("RANGE SLIDER VALUES ${rangeSlider.values} ${value.toInt()} price: ${slider.values[1].toInt() - slider.values[0].toInt()}")
        }
        priceTo.text = rangeSlider.values[1].toInt().toString()+" "
        priceFrom.text = rangeSlider.values[0].toInt().toString()
        if(rangeSlider.values[1].toInt()==2010){
            priceTo.text = "2000+"
        }
        return view
    }

}