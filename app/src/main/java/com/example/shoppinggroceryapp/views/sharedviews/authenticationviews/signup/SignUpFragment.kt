package com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.signup

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.setPadding
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
import com.core.domain.user.User
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
import com.example.shoppinggroceryapp.helpers.alertdialog.DataLossAlertDialog
import com.example.shoppinggroceryapp.helpers.permissionhandler.CameraPermissionHandler
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageHandler
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageLoaderAndGetter
import com.example.shoppinggroceryapp.helpers.permissionhandler.interfaces.ImagePermissionHandler
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.helpers.inputvalidators.interfaces.InputChecker
import com.example.shoppinggroceryapp.helpers.inputvalidators.TextLayoutInputChecker
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class SignUpFragment : Fragment() {

    private lateinit var firstName:TextInputEditText
    private lateinit var lastName:TextInputEditText
    private lateinit var email:TextInputEditText
    private lateinit var phone:TextInputEditText
    private lateinit var password:TextInputEditText
    private lateinit var confirmedPassword:TextInputEditText
    private lateinit var signUp:MaterialButton
    private lateinit var addProfileImage:ImageView
    private lateinit var firstNameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var phoneLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout
    private lateinit var addProfileBtn:MaterialButton
    private lateinit var signUpTopbar:MaterialToolbar
    private lateinit var imageHandler: ImageHandler
    private lateinit var imageLoader: ImageLoaderAndGetter
    private var profileUri:String =""
    private var image: Bitmap? = null
    private var isRetailer= false
    private lateinit var permissionHandler: ImagePermissionHandler
    private lateinit var inputChecker: InputChecker
    private lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputChecker = TextLayoutInputChecker()
        imageHandler = ImageHandler(this)
        permissionHandler = CameraPermissionHandler(this,imageHandler)
        permissionHandler.initPermissionResult()
        imageLoader = ImageLoaderAndGetter()
        imageHandler.initActivityResults()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        setUpViewModel()
        initViews(view)
        addTextChangeListeners()
        initClickListeners()

        if(MainActivity.isRetailer){
            signUpTopbar.title = "Add New Retailer"
            isRetailer = true
            signUp.text = "Add Retailer"
        }

        imageHandler.gotImage.observe(viewLifecycleOwner){
            image = it
            val imageName = System.currentTimeMillis().toString()
            addProfileImage.setImageBitmap(image)
            addProfileImage.setPadding(0)
            profileUri = imageName

        }


//        SIGN UP Observer
        signUpViewModel.registrationStatusInt.observe(viewLifecycleOwner){
            doRegistrationProcess(it,isRetailer)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                inputChecker()
            }
        })
        return view
    }

    private fun setUpViewModel() {
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        signUpViewModel = ViewModelProvider(this, GroceryAppSharedVMFactory(retailerDao,userDao))[SignUpViewModel::class.java]

    }

    private fun initClickListeners() {

        signUpTopbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        addProfileBtn.setOnClickListener{
            permissionHandler.checkPermission(false)
        }

        addProfileImage.setOnClickListener{
            permissionHandler.checkPermission(false)
        }

        signUp.setOnClickListener {
            performInputErrorCheck()
            if(passwordLayout.error==null) {
                if (!((password.text.toString()
                        .isNotEmpty()) && (confirmedPassword.text.toString() == password.text.toString()))
                ) {
                    confirmPasswordLayout.error = "Check Both the Passwords Are Same"
                }
            }
            if((phoneLayout.error==null)&&(emailLayout.error==null)&&(passwordLayout.error==null)&&(firstNameLayout.error==null)&&(confirmPasswordLayout.error==null)){
                signUpViewModel.registerNewUser(
                    User(
                        0,
                        profileUri,
                        firstName.text.toString(),
                        lastName.text.toString(),
                        email.text.toString(),
                        phone.text.toString(),
                        password.text.toString(),
                        "",
                        isRetailer
                    )
                )
            }
            clearAllInputFocus()
        }
    }

    private fun performInputErrorCheck() {
        phoneLayout.error = inputChecker.lengthAndEmptyCheckForPhone("Phone Number",phone,10)
        emailLayout.error = inputChecker.lengthAndEmailCheck(email)
        passwordLayout.error = inputChecker.lengthAndEmptyCheck("Password",password,6)
        firstNameLayout.error = inputChecker.lengthAndEmptyCheck("Name",firstName,3)
    }

    private fun clearAllInputFocus() {
        email.clearFocus()
        phone.clearFocus()
        password.clearFocus()
        firstName.clearFocus()
        lastName.clearFocus()
        confirmedPassword.clearFocus()
    }

    private fun addTextChangeListeners() {
//        Text Change Listeners
        val map = mutableMapOf<TextInputEditText,TextInputLayout>()
        map[email] = emailLayout
        map[password] = passwordLayout
        map[phone] = phoneLayout
        map[firstName] = firstNameLayout
        for((editText,layout) in map){
            editText.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    layout.error = null
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    layout.error = null
                }

                override fun afterTextChanged(s: Editable?) {
                    layout.error = null
                }
            })
        }

//        Focus Change Listeners
        confirmedPassword.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                confirmPasswordLayout.error = null
            }
        }
    }

    private fun initViews(view: View) {
        firstName = view.findViewById(R.id.firstName)
        lastName = view.findViewById(R.id.signUpLastName)
        email = view.findViewById(R.id.signUpEmail)
        phone = view.findViewById(R.id.signUpPhoneNumber)
        password =view.findViewById(R.id.signUpPassword)
        confirmedPassword = view.findViewById(R.id.signUpConfirmPassword)
        addProfileBtn = view.findViewById(R.id.addPictureBtn)
        addProfileImage = view.findViewById(R.id.addPictureImage)
        signUpTopbar = view.findViewById(R.id.topbar)
        firstNameLayout = view.findViewById(R.id.signUpFirstNameLayout)
        phoneLayout = view.findViewById(R.id.signUpPhoneNumberLayout)
        emailLayout = view.findViewById(R.id.signUpEmailLayout)
        passwordLayout = view.findViewById(R.id.signUpPasswordLayout)
        confirmPasswordLayout = view.findViewById(R.id.signUpConfirmPasswordLayout)
        signUp = view.findViewById(R.id.signUpNewUser)
    }

    private fun doRegistrationProcess(status: Int,isRetailer:Boolean) {
        when(status){
            0 ->{
                var text =""
                if(isRetailer) {
                    text = "Admin Added Successfully"
                }
                else{
                    text ="User Added Successfully"
                }
                ShowShortToast.show(text,requireContext())
                image?.let {
                    imageLoader.storeImageInApp(requireContext(),it,profileUri)
                }
                parentFragmentManager.popBackStack()
            }
            1 -> {
                ShowShortToast.show("Phone Number and Email is Already Registered",requireContext())
            }
            2 -> {
                ShowShortToast.show("Phone Number is Already Registered",requireContext())
            }
            3 -> {
                ShowShortToast.show("Email is Already Registered",requireContext())
            }
            else -> {
                ShowShortToast.show("Something Went Wrong",requireContext())
            }
        }
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

    fun inputChecker(){
        if(firstName.text.toString().isEmpty() && lastName.text.toString().isEmpty() && email.text.toString().isEmpty()
            && password.text.toString().isEmpty() && confirmedPassword.text.toString().isEmpty() && phone.text.toString().isEmpty() && profileUri.isEmpty()){
            parentFragmentManager.popBackStack()
        }
        else{
            DataLossAlertDialog().showDataLossAlertDialog(requireContext(),parentFragmentManager)
        }
    }
}