package com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.login

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
import com.example.shoppinggroceryapp.framework.db.entity.user.UserEntity
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.helpers.inputvalidators.interfaces.InputChecker
import com.example.shoppinggroceryapp.helpers.inputvalidators.TextLayoutInputChecker
import com.example.shoppinggroceryapp.helpers.snackbar.ShowShortSnackBar
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.signup.SignUpFragment
import com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.forgotpassword.ForgotPasswordFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class LoginFragment : Fragment() {

    private lateinit var emailPhoneTextLayout:TextInputLayout
    private lateinit var loginButton:MaterialButton
    private lateinit var passwordLayout:TextInputLayout
    private lateinit var emailPhoneText:TextInputEditText
    private lateinit var password:TextInputEditText
    private lateinit var forgotPassword:MaterialButton
    private lateinit var signUp:MaterialButton
    private var login = false
    private lateinit var inputChecker: InputChecker
    private lateinit var handler: Handler
    private lateinit var loginViewModel: LoginViewModel
    private lateinit  var userEntityObserver:Observer<UserEntity?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputChecker = TextLayoutInputChecker()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        initViews(view)
        setClickListeners()
        setUpViewModel()
        setLoginViewModelObservers()
        setFocusChangeListeners()
        return view
    }



    private fun setFocusChangeListeners() {
        emailPhoneText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                emailPhoneTextLayout.error = null
            }
        }
        password.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                passwordLayout.error = null
            }
        }
    }

    private fun setLoginViewModelObservers() {
        loginViewModel.userName.observe(viewLifecycleOwner){
            if(it == null){
                ShowShortSnackBar.showRedColor(requireView(),"User Not Found")
            }
            else{
                loginViewModel.validateUser(emailPhoneText.text.toString().trim(),password.text.toString())
            }
        }

        loginViewModel.user.observe(viewLifecycleOwner){
            if(it==null){
                ShowShortSnackBar.showRedColor(requireView(),"InValid Password")
            }
            else{
                val sharedPreferences = requireActivity().getSharedPreferences("freshCart",Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isSigned",true)
                editor.putBoolean("isRetailer",it.isRetailer)
                editor.putString("userFirstName",it.userFirstName)
                editor.putString("userLastName",it.userLastName)
                editor.putString("userEmail",it.userEmail)
                editor.putString("userPhone",it.userPhone)
                editor.putString("userId",it.userId.toString())
                editor.putString("userProfile",it.userImage)
                editor.apply()
                loginViewModel.assignCartForUser()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentBody, InitialFragment())
                    .commit()
            }
        }
    }

    private fun setClickListeners() {
        forgotPassword.setOnClickListener {
            password.text = null
            emailPhoneText.text = null
            passwordLayout.error = null
            emailPhoneTextLayout.error = null
            loginViewModel.userName = MutableLiveData()
            loginViewModel.user = MutableLiveData()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentBody, ForgotPasswordFragment())
                .addToBackStack("Sign Up Fragment")
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .commit()
        }

        loginButton.setOnClickListener {
            emailPhoneTextLayout.error = inputChecker.emptyCheck(emailPhoneText)
            passwordLayout.error = inputChecker.emptyCheck(password)
            emailPhoneText.clearFocus()
            password.clearFocus()
            if(emailPhoneTextLayout.error == null && passwordLayout.error==null) {
                loginViewModel.isUser(emailPhoneText.text.toString().trim())
            }
        }


        signUp.setOnClickListener {
            password.text = null
            emailPhoneText.text = null
            passwordLayout.error = null
            emailPhoneTextLayout.error = null
            loginViewModel.userName = MutableLiveData()
            loginViewModel.user = MutableLiveData()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentBody, SignUpFragment())
                .addToBackStack("Sign Up Fragment")
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .commit()
        }
    }

    override fun onStop() {
        super.onStop()
        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
    }
    override fun onResume() {
        super.onResume()
        InitialFragment.hideSearchBar.value = true
        InitialFragment.hideBottomNav.value = true
    }
    private fun initViews(view: View) {
        emailPhoneText = view.findViewById(R.id.emailOrMobile)
        handler = Handler(Looper.getMainLooper())
        password = view.findViewById(R.id.password)
        emailPhoneTextLayout = view.findViewById(R.id.emailLayout)
        passwordLayout = view.findViewById(R.id.passwordLayout)
        signUp = view.findViewById(R.id.signUpBtn)
        loginButton = view.findViewById(R.id.loginButton)
        forgotPassword = view.findViewById(R.id.forgotPassword)
    }


    private fun setUpViewModel() {
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        loginViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[LoginViewModel::class.java]
    }
}