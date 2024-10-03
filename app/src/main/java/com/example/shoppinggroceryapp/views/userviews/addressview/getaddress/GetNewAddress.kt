package com.example.shoppinggroceryapp.views.userviews.addressview.getaddress

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.core.data.repository.AddressRepository
import com.core.data.repository.AuthenticationRepository
import com.core.data.repository.CartRepository
import com.core.data.repository.HelpRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.data.repository.SearchRepository
import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.user.Address
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.data.authentication.AuthenticationDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.address.AddressDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.cart.CartDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.help.HelpDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.order.OrderDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.product.ProductDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.search.SearchDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.subscription.SubscriptionDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.user.UserDataSourceImpl
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.helpers.inputvalidators.interfaces.InputChecker
import com.example.shoppinggroceryapp.helpers.inputvalidators.TextLayoutInputChecker
import com.example.shoppinggroceryapp.views.GroceryAppViewModelFactory
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
        fullNameLayout = view.findViewById(R.id.addressFirstNameLayout)
        phoneLayout = view.findViewById(R.id.addressPhoneNumberLayout)
        houseLayout =view.findViewById(R.id.addressHouseLayout)
        streetLayout = view.findViewById(R.id.addressStreetLayout)
        cityLayout = view.findViewById(R.id.addressCityLayout)
        stateLayout = view.findViewById(R.id.addressStateLayout)
        postalCodeLayout = view.findViewById(R.id.addressPostalCodeLayout)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        val userRepository = UserRepository(UserDataSourceImpl(userDao))
        val authenticationRepository = AuthenticationRepository(AuthenticationDataSourceImpl(userDao))
        val cartRepository: CartRepository = CartRepository(CartDataSourceImpl(userDao))
        val helpRepository: HelpRepository = HelpRepository(
            HelpDataSourceImpl(retailerDao),
            HelpDataSourceImpl(retailerDao)
        )
        val orderRepository: OrderRepository = OrderRepository(
            OrderDataSourceImpl(retailerDao),
            OrderDataSourceImpl(retailerDao)
        )
        val productRepository: ProductRepository = ProductRepository(
            ProductDataSourceImpl(retailerDao),
            ProductDataSourceImpl(retailerDao)
        )
        val searchRepository: SearchRepository = SearchRepository(SearchDataSourceImpl(userDao))
        val subscriptionRepository: SubscriptionRepository = SubscriptionRepository(
            SubscriptionDataSourceImpl(userDao),
            SubscriptionDataSourceImpl(userDao),
            SubscriptionDataSourceImpl(userDao)
        )
        val addressRepository: AddressRepository = AddressRepository(AddressDataSourceImpl(userDao))
        getAddressViewModel = ViewModelProvider(this, GroceryAppViewModelFactory(userRepository, authenticationRepository, cartRepository, helpRepository, orderRepository, productRepository, searchRepository, subscriptionRepository, addressRepository))[GetAddressViewModel::class.java]
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
        if(SavedAddressList.editAddressEntity !=null){
            SavedAddressList.editAddressEntity?.let {
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
            parentFragmentManager.popBackStack()
        }
        addFocusChangeListeners()
        saveAddress.setOnClickListener {
            fullNameLayout.error = addressInputChecker.nameCheck(fullName)
            phoneLayout.error = addressInputChecker.lengthAndEmptyCheck("Phone Number",phone,10)
            houseLayout.error = addressInputChecker.emptyCheck(houseNo)
            streetLayout.error = addressInputChecker.emptyCheck(street)
            cityLayout.error = addressInputChecker.emptyCheck(city)
            stateLayout.error = addressInputChecker.emptyCheck(state)
            postalCodeLayout.error = addressInputChecker.lengthAndEmptyCheck("Zip Code",postalCode,6)
            if(fullNameLayout.error==null && houseLayout.error==null && phoneLayout.error==null &&streetLayout.error==null &&
                cityLayout.error ==null && stateLayout.error==null && postalCodeLayout.error == null){
                if(SavedAddressList.editAddressEntity !=null){
                    getAddressViewModel.updateAddress(
                    Address(
                        addressId = SavedAddressList.editAddressEntity!!.addressId,
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

                    Toast.makeText(context,"Address Updated Successfully",Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context,"Address Added Successfully",Toast.LENGTH_SHORT).show()
                }

                parentFragmentManager.popBackStack()
            }
            else{
                Toast.makeText(context,"Add all the Required Fields",Toast.LENGTH_SHORT).show()
            }
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
        SavedAddressList.editAddressEntity = null
    }

    fun addFocusChangeListeners(){
        fullName.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                fullNameLayout.error = null
            }
        }
        phone.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                phoneLayout.error = null
            }
        }
        houseNo.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                houseLayout.error = null
            }
        }
        street.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                streetLayout.error = null
            }
        }
        city.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                cityLayout.error = null
            }
        }
        state.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                stateLayout.error = null
            }
        }
        postalCode.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                postalCodeLayout.error = null
            }
        }

    }
}