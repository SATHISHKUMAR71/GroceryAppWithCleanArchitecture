package com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.core.domain.user.Address
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.MainActivity.Companion.selectedAddress
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.framework.db.entity.user.AddressEntity
import com.example.shoppinggroceryapp.helpers.extensions.putAddress
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.example.shoppinggroceryapp.views.userviews.addressview.getaddress.GetNewAddress
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.SavedAddressList
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment
import com.google.android.material.button.MaterialButton

class AddressAdapter(var addressEntityList: List<Address>, var fragment: Fragment):RecyclerView.Adapter<AddressAdapter.AddressHolder>() {

    var getNewAddress = GetNewAddress()
    var selectedAddressPositions = mutableListOf<Boolean>()

    companion object{
        var clickable = false
    }
    init {
        for(i in addressEntityList.indices){
            if(i== selectedAddress){
                selectedAddressPositions.add(true)
            }
            selectedAddressPositions.add(false)
        }
    }
    inner class AddressHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val contactName = itemView.findViewById<TextView>(R.id.addressOwnerName)
        val address = itemView.findViewById<TextView>(R.id.address)
        val contactNumber = itemView.findViewById<TextView>(R.id.addressPhone)
        val editAddress = itemView.findViewById<MaterialButton>(R.id.editAddressButton)
        val checkedAddress = itemView.findViewById<RadioButton>(R.id.itemSelectedAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressHolder {
        return AddressHolder(LayoutInflater.from(parent.context).inflate(R.layout.address_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return addressEntityList.size
    }

    override fun onBindViewHolder(holder: AddressHolder, position: Int) {
        println("ADDRESS LIST: $selectedAddressPositions")
        val address = "${addressEntityList[position].buildingName}, ${addressEntityList[position].streetName}," +
                "${addressEntityList[position].city}, ${addressEntityList[position].state}, ${addressEntityList[position].postalCode}"
            holder.address.text = address
            holder.contactName.text = addressEntityList[position].addressContactName
            holder.contactNumber.text = addressEntityList[position].addressContactNumber
        holder.checkedAddress.isChecked = selectedAddressPositions[position]
        holder.editAddress.setOnClickListener {
            getNewAddress.arguments = Bundle().apply {
                this.putInt("addressId",addressEntityList[position].addressId)
                this.putString("addressContactName",addressEntityList[position].addressContactName)
                this.putString("addressContactNumber",addressEntityList[position].addressContactNumber)
                this.putString("buildingName",addressEntityList[position].buildingName)
                this.putString("streetName",addressEntityList[position].streetName)
                this.putString("city",addressEntityList[position].city)
                this.putString("state",addressEntityList[position].state)
                this.putString("postalCode",addressEntityList[position].postalCode)
            }
            FragmentTransaction.navigateWithBackstack(fragment.parentFragmentManager, getNewAddress,"Get New Address Fragment")
        }
        if(clickable){
            holder.checkedAddress.visibility = View.VISIBLE
            holder.itemView.setOnClickListener {
                saveAddressPosition(position)
                CartFragment.selectedAddressPosition = position
                CartFragment.selectedAddressEntity = addressEntityList[position]
                clickable =false
                ShowShortToast.show("Address Changed Successfully",fragment.requireContext())
                fragment.parentFragmentManager.popBackStack()
            }
        }
        holder.checkedAddress.setOnClickListener {
            saveAddressPosition(position)
            selectedAddressPositions[position] = true
            for(i in 0..<selectedAddressPositions.size){
                if(position==i){
                    selectedAddressPositions[i] = true
                }
                else{
                    selectedAddressPositions[i] = false
                }
            }
            notifyItemRangeChanged(0,addressEntityList.size)
            CartFragment.selectedAddressPosition = position
            CartFragment.selectedAddressEntity = addressEntityList[position]
            clickable =false
            fragment.parentFragmentManager.popBackStack()
        }
    }

    fun saveAddressPosition(pos:Int){
        val pref = fragment.requireActivity().getSharedPreferences("freshCart", Context.MODE_PRIVATE)
        val editor =pref.edit()
        editor.putInt("selectedAddress${MainActivity.userId}",pos)
        selectedAddress = pos
        editor.apply()
    }
}

