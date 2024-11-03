package com.example.shoppinggroceryapp.views.userviews.addressview.getaddress

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.core.domain.user.Address
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.alertdialog.DataLossAlertDialog
import com.example.shoppinggroceryapp.helpers.extensions.getAddress
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.helpers.inputvalidators.interfaces.InputChecker
import com.example.shoppinggroceryapp.helpers.inputvalidators.TextLayoutInputChecker
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.example.shoppinggroceryapp.views.GroceryAppUserVMFactory
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.SavedAddressList
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class GetNewAddress : Fragment() {

    private lateinit var fullName: TextInputEditText
    private lateinit var fullNameLayout:TextInputLayout
    private lateinit var phoneLayout:TextInputLayout
    private lateinit var houseLayout:TextInputLayout
    private lateinit var streetLayout: TextInputLayout
    private lateinit var cityLayout: TextInputLayout
    private lateinit var stateLayout: TextInputLayout
    private lateinit var postalCodeLayout: TextInputLayout
    private lateinit var phone: TextInputEditText
    private lateinit var houseNo: TextInputEditText
    private lateinit var street: TextInputEditText
    private lateinit var state: AutoCompleteTextView
    private lateinit var city: TextInputEditText
    private lateinit var postalCode: TextInputEditText
    private lateinit var saveAddress:MaterialButton
    private lateinit var addressTopBar:MaterialToolbar
    private lateinit var addressInputChecker: InputChecker
    private lateinit var getAddressViewModel: GetAddressViewModel
    private var editAddress :Address? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addressInputChecker = TextLayoutInputChecker()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_get_address, container, false)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        getAddressViewModel = ViewModelProvider(this, GroceryAppUserVMFactory(userDao, retailerDao))[GetAddressViewModel::class.java]
        initViews(view)
        editAddress= arguments?.let {
            Address(it.getInt("addressId"),MainActivity.userId.toInt(),it.getString("addressContactName",""),it.getString("addressContactNumber",""),
                it.getString("buildingName",""),it.getString("streetName",""),it.getString("city",""),it.getString("state",""),"India",
                it.getString("postalCode",""))
        }
//        if(SavedAddressList.editAddressEntity !=null){
//            SavedAddressList.editAddressEntity?.let {
//                fullName.setText(it.addressContactName)
//                phone.setText(it.addressContactNumber)
//                houseNo.setText(it.buildingName)
//                street.setText(it.streetName)
//                state.setText(it.state)
//                city.setText(it.city)
//                postalCode.setText(it.postalCode)
//            }
//        }
        if(editAddress !=null){
            editAddress?.let {
                addressTopBar.title = "Edit Address"
                saveAddress.text = "Update Address"
                fullName.setText(it.addressContactName)
                phone.setText(it.addressContactNumber)
                houseNo.setText(it.buildingName)
                street.setText(it.streetName)
                state.setText(it.state)
                city.setText(it.city)
                postalCode.setText(it.postalCode)
            }
        }
        addressTopBar.setNavigationOnClickListener {
            inputChangeChecker()
//            DataLossAlertDialog().showDataLossAlertDialog(requireContext(),parentFragmentManager)
//            parentFragmentManager.popBackStack()
        }
        addFocusChangeListeners()
        saveAddress.setOnClickListener {
            validateInput()
            if(fullNameLayout.error==null && houseLayout.error==null && phoneLayout.error==null &&streetLayout.error==null &&
                cityLayout.error ==null && stateLayout.error==null && postalCodeLayout.error == null){
                if(editAddress !=null){
                    getAddressViewModel.updateAddress(
                        Address(
                            addressId = editAddress!!.addressId,
                            userId = MainActivity.userId.toInt(),
                            addressContactName = fullName.text.toString(),
                            addressContactNumber = phone.text.toString(),
                            buildingName = houseNo.text.toString(),
                            streetName = street.text.toString(),
                            city = city.text.toString(),
                            state = state.text.toString(),
                            country = "India",
                            postalCode = postalCode.text.toString()
                        )
                    )
                    ShowShortToast.show("Address Updated Successfully",requireContext())
                }
                else {
                    getAddressViewModel.addAddress(
                        Address(
                            addressId = 0,
                            userId = MainActivity.userId.toInt(),
                            addressContactName = fullName.text.toString(),
                            addressContactNumber = phone.text.toString(),
                            buildingName = houseNo.text.toString(),
                            streetName = street.text.toString(),
                            city = city.text.toString(),
                            state = state.text.toString(),
                            country = "India",
                            postalCode = postalCode.text.toString()
                        )
                    )
                    ShowShortToast.show("Address Added Successfully",requireContext())
                }
                parentFragmentManager.popBackStack()
            }
            else{
                ShowShortToast.show("Add all the Required Fields",requireContext())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                inputChangeChecker()
//                DataLossAlertDialog().showDataLossAlertDialog(requireContext(), parentFragmentManager = parentFragmentManager)
            }
        })

        return view
    }

    private fun validateInput() {
        fullNameLayout.error = addressInputChecker.nameCheck(fullName)
        phoneLayout.error = addressInputChecker.lengthAndEmptyCheckForPhone("Phone Number",phone,10)
        houseLayout.error = addressInputChecker.emptyCheck(houseNo)
        streetLayout.error = addressInputChecker.emptyCheck(street)
        cityLayout.error = addressInputChecker.emptyCheck(city)
        stateLayout.error = addressInputChecker.emptyCheck(state)
        postalCodeLayout.error = addressInputChecker.lengthAndEmptyCheck("Zip Code",postalCode,6)
    }

    private fun initViews(view: View) {
        fullNameLayout = view.findViewById(R.id.addressFirstNameLayout)
        phoneLayout = view.findViewById(R.id.addressPhoneNumberLayout)
        houseLayout =view.findViewById(R.id.addressHouseLayout)
        streetLayout = view.findViewById(R.id.addressStreetLayout)
        cityLayout = view.findViewById(R.id.addressCityLayout)
        stateLayout = view.findViewById(R.id.addressStateLayout)
        postalCodeLayout = view.findViewById(R.id.addressPostalCodeLayout)
        fullName = view.findViewById(R.id.fullName)
        phone = view.findViewById(R.id.addPhoneNumber)
        houseNo = view.findViewById(R.id.addAddressHouse)
        street = view.findViewById(R.id.addAddressStreetName)
        state = view.findViewById(R.id.addAddressState)
        city = view.findViewById(R.id.addAddressCity)
        postalCode = view.findViewById(R.id.addAddressPostalCode)
        saveAddress = view.findViewById(R.id.addNewAddress)
        addressTopBar = view.findViewById(R.id.getAddressToolbar)
        phone.filters = arrayOf(InputFilter.LengthFilter(15))
        postalCode.filters = arrayOf(InputFilter.LengthFilter(8))
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
//        SavedAddressList.editAddressEntity = null
    }

    private fun addFocusChangeListeners(){
        val layoutWithEditText = mutableMapOf<TextInputEditText,TextInputLayout>()
        layoutWithEditText[fullName] = fullNameLayout
        layoutWithEditText[phone] = phoneLayout
        layoutWithEditText[houseNo] = houseLayout
        layoutWithEditText[street] = streetLayout
        layoutWithEditText[city] = cityLayout
        layoutWithEditText[postalCode] = postalCodeLayout
        for((editText,layout) in layoutWithEditText){
            editText.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    layout.error = null
                }
            }
        }
        state.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                stateLayout.error = null
            }
        }
    }

    fun inputChangeChecker(){
        editAddress?.let {
            if(it.city==city.text.toString() && it.state == state.text.toString() && it.addressContactNumber==phone.text.toString()
                && it.buildingName==houseNo.text.toString() && it.addressContactName==fullName.text.toString() && it.streetName==street.text.toString()
                && it.postalCode == postalCode.text.toString()){
                parentFragmentManager.popBackStack()
                return
            }
            else{
                DataLossAlertDialog().showDataLossAlertDialog(requireContext(),parentFragmentManager)
                return
            }
        }
        if(city.text.toString().isEmpty() && state.text.toString().isEmpty() && phone.text.toString().isEmpty()
            && houseNo.text.toString().isEmpty() && fullName.text.toString().isEmpty() && street.text.toString().isEmpty()
            && postalCode.text.toString().isEmpty()){
            parentFragmentManager.popBackStack()
        }
        else{
            DataLossAlertDialog().showDataLossAlertDialog(requireContext(),parentFragmentManager)
            return
        }

    }

}