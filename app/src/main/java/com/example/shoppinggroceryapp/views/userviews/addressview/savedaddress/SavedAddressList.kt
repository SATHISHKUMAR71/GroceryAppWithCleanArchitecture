package com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.data.repository.CustomerRepository
import com.core.data.repository.RetailerRepository
import com.core.data.repository.UserRepository
import com.core.domain.user.Address
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.data.CustomerDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.RetailerDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.UserDataSourceImpl
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.framework.db.entity.user.AddressEntity
import com.example.shoppinggroceryapp.views.GroceryAppViewModelFactory
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.adapter.AddressAdapter
import com.example.shoppinggroceryapp.views.userviews.addressview.getaddress.GetNewAddress
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton


class SavedAddressList : Fragment() {

    private lateinit var addressRV:RecyclerView
    private lateinit var db: AppDatabase

    private lateinit var handler:Handler
    private lateinit var addressCount:TextView
    private lateinit var savedAddressToolbar:MaterialToolbar
    companion object{
        var editAddressEntity: Address? = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_saved_address, container, false)
        handler = Handler(Looper.getMainLooper())
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val retailerRepository = RetailerRepository(RetailerDataSourceImpl(db1.getRetailerDao()))
        val customerRepository = CustomerRepository(CustomerDataSourceImpl(db1.getUserDao()))
        val userRepository = UserRepository(UserDataSourceImpl(db1.getUserDao(),db1.getRetailerDao()))

        val savedAddressViewModel = ViewModelProvider(this,
            GroceryAppViewModelFactory(userRepository, retailerRepository, customerRepository)
        )[SavedAddressViewModel::class.java]
        addressRV = view.findViewById(R.id.savedAddressList)
        addressCount = view.findViewById(R.id.countAddress)
        savedAddressToolbar = view.findViewById(R.id.savedAddressToolbar)
        savedAddressToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        if(arguments?.getBoolean("clickable")==true){
            AddressAdapter.clickable = true
        }
        db = AppDatabase.getAppDatabase(requireContext())
        val userId = MainActivity.userId.toInt()

        savedAddressViewModel.addressEntityList.observe(viewLifecycleOwner){
            val count = "No of Saved Address: ${it.size}"
            addressCount.text = count
            addressRV.adapter = AddressAdapter(it,this)
            addressRV.layoutManager = LinearLayoutManager(context)
        }
        savedAddressViewModel.getAddressListForUser(userId)
        view.findViewById<MaterialButton>(R.id.addNewAddress).setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, GetNewAddress(),"Get Address")
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        InitialFragment.hideSearchBar.value = true
        InitialFragment.hideBottomNav.value = true
    }
    override fun onStop() {
        super.onStop()
        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
    }
}