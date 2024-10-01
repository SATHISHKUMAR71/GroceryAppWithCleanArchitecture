package com.example.shoppinggroceryapp.views.retailerviews.customerrequestdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist.CustomerRequestListFragment
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail.OrderDetailFragment
import com.google.android.material.appbar.MaterialToolbar

class CustomerRequestDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_request_detail, container, false)
        val orderDetailFrag = OrderDetailFragment()

        orderDetailFrag.arguments = Bundle().apply {
            putBoolean("hideToolBar",true)
        }
        view.findViewById<MaterialToolbar>(R.id.customerRequestToolbar).setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        view.findViewById<TextView>(R.id.customerName).text =
            CustomerRequestListFragment.customerName
        val email = "Email: ${CustomerRequestListFragment.customerEmail}"
        val phone = "Phone: ${CustomerRequestListFragment.customerPhone}"
        view.findViewById<TextView>(R.id.customerEmail).text = email
        view.findViewById<TextView>(R.id.customerPhone).text = phone
        view.findViewById<TextView>(R.id.customerRequestText).text =
            CustomerRequestListFragment.customerRequest
        val requestedOn ="Requested On: ${CustomerRequestListFragment.requestedDate}"
        view.findViewById<TextView>(R.id.requestedDate).text = requestedOn
        parentFragmentManager.beginTransaction()
            .replace(R.id.orderDetailsFragment,orderDetailFrag)
            .commit()
        return view
    }

    override fun onStop() {
        super.onStop()

        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
    }
}