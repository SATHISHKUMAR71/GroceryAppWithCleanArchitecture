package com.example.shoppinggroceryapp.views.retailerviews.addeditproduct


import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
import com.core.domain.products.Category
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product
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
import com.example.shoppinggroceryapp.framework.db.dataclass.IntWithCheckedData
import com.example.shoppinggroceryapp.helpers.NotificationBuilder
import com.example.shoppinggroceryapp.helpers.alertdialog.DataLossAlertDialog
import com.example.shoppinggroceryapp.helpers.permissionhandler.CameraPermissionHandler
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageHandler
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageLoaderAndGetter
import com.example.shoppinggroceryapp.helpers.permissionhandler.interfaces.ImagePermissionHandler
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.helpers.inputvalidators.interfaces.InputChecker
import com.example.shoppinggroceryapp.helpers.inputvalidators.TextLayoutInputChecker
import com.example.shoppinggroceryapp.helpers.snackbar.ShowShortSnackBar
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.example.shoppinggroceryapp.views.GroceryAppRetailerVMFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Locale

class AddOrEditProductFragment : Fragment() {


    private lateinit var imageHandler: ImageHandler
    private lateinit var imageLoader: ImageLoaderAndGetter
    private var isNewParentCategory = false
    private var isNewSubCategory = false
    var oldMainImage = ""
    var oldAvailableItems = 0
    private lateinit var imagePermissionHandler: ImagePermissionHandler
    private var mainImage:String = ""
    private var mainImageBitmap:Bitmap?= null
    private lateinit var childArray:Array<String>
    private lateinit var parentArray:Array<String>
    private var isCategoryImageAdded = true
    private var rawExpiryDate = ""
    private var rawManufactureDate = ""
    private lateinit var inputChecker: InputChecker
    var count = 0
    var editingProduct = false
    var fileName=""
    var mainImageClicked = false
    var parentCategoryImageClicked = false
    var parentCategoryImage:Bitmap? = null
    var modifiedProduct:Product? = null
    var imageList = mutableMapOf<Int, IntWithCheckedData>()
    var imageStringList = mutableListOf<String>()
    private lateinit var addParentCategoryButton:MaterialButton
    private lateinit var addParentImage:ImageView
    private lateinit var productManufactureDate:TextInputEditText
    private lateinit var productExpiryDate:TextInputEditText
    private lateinit var productParentCategory:MaterialAutoCompleteTextView
    private lateinit var productSubCat:MaterialAutoCompleteTextView
    private lateinit var addParentCategoryLayout:LinearLayout
    private lateinit var addEditProductViewModel: AddEditProductViewModel
    private lateinit var productName:TextInputEditText
    private lateinit var brandName:TextInputEditText
    private lateinit var updateBtn:MaterialButton
    private lateinit var productDescription:TextInputEditText
    private lateinit var productPrice:TextInputEditText
    private lateinit var productOffer:TextInputEditText
    private lateinit var productQuantity:TextInputEditText
    private lateinit var productAvailableItems:TextInputEditText
    private lateinit var isVeg:CheckBox
    private lateinit var imageLayout:LinearLayout
    private lateinit var view:View
    private var deletedImageList = mutableListOf<String>()
    private lateinit var formatter:SimpleDateFormat
    private lateinit var productNameLayout:TextInputLayout
    private lateinit var brandNameLayout:TextInputLayout
    private lateinit var parentCategoryLayout: TextInputLayout
    private lateinit var subCategoryLayout: TextInputLayout
    private lateinit var productDescriptionLayout:TextInputLayout
    private lateinit var priceLayout:TextInputLayout
    private lateinit var offerLayout:TextInputLayout
    private lateinit var productQuantityLayout:TextInputLayout
    private lateinit var availableItemsLayout:TextInputLayout
    private lateinit var manufactureDateLayout:TextInputLayout
    private lateinit var expiryDateLayout:TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageHandler = ImageHandler(this)
        imageHandler.initActivityResults()
        inputChecker = TextLayoutInputChecker()
        imagePermissionHandler = CameraPermissionHandler(this,imageHandler)
        imagePermissionHandler.initPermissionResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setUpViewModel()
        view =  inflater.inflate(R.layout.fragment_add_edit, container, false)
        initViews(view)
        setUpCategoryListeners()
        setUpTextFocusListeners()

        formatter = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        ProductListFragment.selectedProductEntity.value?.let {

            setInitialValues(it)
        }
        setUpObservers()

        val dateManufacturePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select the Manufactured Date")
            .setTextInputFormat(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()

        val dateExpiryPicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select the Expiry Date")
            .setTextInputFormat(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                inputChecker()
            }
        })

        dateManufacturePicker.addOnPositiveButtonClickListener {
            rawManufactureDate = formatter.format(it)
            var res:String? = null
            if(rawExpiryDate.isNotEmpty()){
                println("123321 manufacture date: manu date $rawManufactureDate expiry date: $rawExpiryDate compare: ${
                    DateGenerator.compareDeliveryStatus(
                        rawManufactureDate,
                        rawExpiryDate
                    )
                }"
                )
                res = DateGenerator.compareDeliveryStatus(
                    rawManufactureDate,
                    rawExpiryDate
                )
            }
            productManufactureDate.setText(DateGenerator.getDayAndMonth(formatter.format(it)))
            res?.let {
                if(it=="Delivered" || it=="Out For Delivery"){
                    productManufactureDate.setText("")
                    ShowShortToast.show("Manufacture Date Should be minimum than expiry date",requireContext())
                }
            }
        }
        dateExpiryPicker.addOnPositiveButtonClickListener {
            var res:String? = null
            rawExpiryDate = formatter.format(it)
            if(rawManufactureDate.isNotEmpty()) {
                println(
                    "123321 manufacture date: manu date $rawManufactureDate expiry date: $rawExpiryDate compare: ${
                        DateGenerator.compareDeliveryStatus(
                            rawManufactureDate,
                            rawExpiryDate
                        )
                    }"
                )
                res = DateGenerator.compareDeliveryStatus(
                    rawManufactureDate,
                    rawExpiryDate
                )
            }
            productExpiryDate.setText(DateGenerator.getDayAndMonth(formatter.format(it)))
            res?.let {
                if(it=="Delivered" || it=="Out For Delivery"){
                    productExpiryDate.setText("")
                    ShowShortToast.show("Expiry Date Should be maximum than Manufacture date",requireContext())
                }
            }
        }

        setUpDatePickerListeners(dateManufacturePicker,dateExpiryPicker)
        setUpTextChangedListeners()
        setUpUpdateButtonListener()
        setUpAddNewImageListeners()
        setUpImageHandlerObserver(container)

        view.findViewById<MaterialToolbar>(R.id.materialToolbarEditProductFrag).setNavigationOnClickListener {
            inputChecker()
        }
        view.findViewById<CheckBox>(R.id.noExpiryDate).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                expiryDateLayout.visibility = View.GONE
            }
            else{
                expiryDateLayout.visibility = View.VISIBLE
            }
        }
//        addEditProductViewModel.alertNotification.observe(viewLifecycleOwner){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val notificationBuilder = NotificationBuilder(requireContext())
//                notificationBuilder.createNotificationChannel()
//                notificationBuilder.showNotification(it,"${it.productName} has less no of stocks","In your Orders Some of the products are less in stocks","Some Items in your order is less in stocks so unable to process that order we will delivery other products if any products in your order")
//            }
//        }

        return view
    }

    private fun setUpObservers() {
        addEditProductViewModel.imageList.observe(viewLifecycleOwner){
            if(editingProduct) {
                for (i in it) {
                    imageStringList.add(i.images)
                    fileName = i.images
                    imageHandler.gotImage.value =
                        imageLoader.getImageInApp(requireContext(), i.images)
                }
                editingProduct = false
                fileName = ""
            }
            else{
                for (i in it) {
                    imageHandler.gotImage.value =
                        imageLoader.getImageInApp(requireContext(), i.images)
                }
            }
        }

        addEditProductViewModel.brandName.observe(viewLifecycleOwner){
            brandName.setText(it)
        }

        addEditProductViewModel.getParentArray()

        addEditProductViewModel.parentArray.observe(viewLifecycleOwner){
            parentArray = it
            if(isNewParentCategory){
                for(i in it){
                    if(i == productParentCategory.text.toString()){
                        addEditProductViewModel.getParentCategoryImageForParent(i)
                        isNewParentCategory = false
                    }
                }
            }
            productParentCategory.setSimpleItems(parentArray)
        }

        ProductListFragment.selectedProductEntity.value?.let {
            addEditProductViewModel.getParentCategory(it.categoryName)
        }

        addEditProductViewModel.parentCategory.observe(viewLifecycleOwner){ parentCategoryValue ->
            productParentCategory.setText(parentCategoryValue)
        }


        addEditProductViewModel.childArray.observe(viewLifecycleOwner){ childItems->
            productSubCat.setSimpleItems(childItems)
            ProductListFragment.selectedProductEntity.value?.let {
                productSubCat.setText(it.categoryName)
            }
        }
    }

    private fun setUpViewModel() {
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        addEditProductViewModel = ViewModelProvider(this,
            GroceryAppRetailerVMFactory(userDao, retailerDao))[AddEditProductViewModel::class.java]
    }

    private fun setInitialValues(it:Product) {
        editingProduct = true
        addEditProductViewModel.getBrandName(it.brandId)
        oldMainImage = it.mainImage
        imageHandler.gotImage.value = imageLoader.getImageInApp(requireContext(),it.mainImage)
        mainImage = it.mainImage
        updateBtn.text = "Update Product"
        view.findViewById<MaterialToolbar>(R.id.materialToolbarEditProductFrag).title = "Update Product"
        mainImageClicked = true
        addEditProductViewModel.getImagesForProduct(it.productId)
        productName.setText(it.productName)
        addEditProductViewModel.getParentCategoryImage(it.categoryName)
        productDescription.setText(it.productDescription)
        productPrice.setText(it.price.toString())
        productOffer.setText(it.offer.toString())
        productSubCat.setText(it.categoryName)
        productQuantity.setText(it.productQuantity)
        oldAvailableItems = it.availableItems
        productAvailableItems.setText(it.availableItems.toString())
        isVeg.isChecked = it.isVeg
        rawExpiryDate = it.expiryDate
        rawManufactureDate = it.manufactureDate
        productManufactureDate.setText(DateGenerator.getDayAndMonth(it.manufactureDate))
        if(it.expiryDate.isNotEmpty()) {
            productExpiryDate.setText(DateGenerator.getDayAndMonth(it.expiryDate))
        }
        else{
            expiryDateLayout.visibility = View.GONE
            view.findViewById<CheckBox>(R.id.noExpiryDate).isChecked = true
        }
    }

    private fun initViews(view: View) {
        parentArray = arrayOf()
        childArray = arrayOf()
        productNameLayout = view.findViewById(R.id.productNameLayout)
        brandNameLayout = view.findViewById(R.id.productBrandLayout)
        parentCategoryLayout = view.findViewById(R.id.productParentCatLayout)
        subCategoryLayout = view.findViewById(R.id.productCategoryLayout)
        productDescriptionLayout = view.findViewById(R.id.productDescriptionLayout)
        priceLayout = view.findViewById(R.id.productPriceLayout)
        offerLayout = view.findViewById(R.id.productOfferLayout)
        productQuantityLayout = view.findViewById(R.id.productQuantityLayout)
        availableItemsLayout = view.findViewById(R.id.productAvailableItemsLayout)
        manufactureDateLayout = view.findViewById(R.id.productManufactureLayout)
        expiryDateLayout = view.findViewById(R.id.productExpiryLayout)
        updateBtn = view.findViewById(R.id.updateInventoryBtn)
        addParentCategoryLayout = view.findViewById(R.id.addParentCategoryImageLayout)
        addParentImage = view.findViewById(R.id.addParentCategoryImage)
        addParentCategoryButton = view.findViewById(R.id.addParentCategoryImageButton)
        productName = view.findViewById(R.id.productNameEditFrag)
        brandName = view.findViewById(R.id.productBrandEditFrag)
        productParentCategory = view.findViewById(R.id.productParentCatEditFrag)
        productSubCat = view.findViewById(R.id.productCategoryEditFrag)
        productDescription = view.findViewById(R.id.productDescriptionEditFrag)
        productPrice = view.findViewById(R.id.productPriceEditFrag)
        productOffer = view.findViewById(R.id.productOfferEditFrag)
        productQuantity = view.findViewById(R.id.productQuantityEditFrag)
        productAvailableItems = view.findViewById(R.id.productAvailableItemsEditFrag)
        isVeg = view.findViewById(R.id.productIsVegEditFrag)
        productManufactureDate = view.findViewById(R.id.productManufactureEditFrag)
        productExpiryDate = view.findViewById(R.id.productExpiryEditFrag)
        imageLayout =view.findViewById(R.id.imageLayout)
        imageLoader = ImageLoaderAndGetter()
    }

    private fun setUpTextFocusListeners(){
        val mapFocusListeners = mutableMapOf<TextInputEditText,TextInputLayout>()
        mapFocusListeners[productName] = productNameLayout
        mapFocusListeners[brandName] = brandNameLayout
        mapFocusListeners[productDescription] = productDescriptionLayout
        mapFocusListeners[productPrice] = priceLayout
        mapFocusListeners[productOffer] = offerLayout
        mapFocusListeners[productQuantity] = productQuantityLayout
        mapFocusListeners[productAvailableItems] = availableItemsLayout
        val textChangeListeners = mutableMapOf<TextInputEditText,TextInputLayout>()
        textChangeListeners[productManufactureDate] = manufactureDateLayout
        textChangeListeners[productExpiryDate] = expiryDateLayout
        for((text,layout) in mapFocusListeners){
            text.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    layout.error = null
                }
            }
        }

        for((date,dateLayout) in textChangeListeners){
            date.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                    if(s.toString().isNotEmpty()){
                        dateLayout.error = null
                    }
                }

            })
        }

        productParentCategory.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                parentCategoryLayout.error = null
            }
        }
        productSubCat.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                subCategoryLayout.error = null
            }
        }
    }

    private fun setUpImageHandlerObserver(container: ViewGroup?) {
        addEditProductViewModel.categoryImage.observe(viewLifecycleOwner){
            it?.let {
                if(it.isNotEmpty()) {
                    val image =imageLoader.getImageInApp(requireContext(), it)
                    addParentImage.setImageBitmap(image)
                    addParentCategoryButton.text = "Change Category Image"
                    isCategoryImageAdded = true
                    if(image==null){
                        addParentImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.add_photo_alternate_24px))
                    }
                }
            }
        }
        imageHandler.gotImage.observe(viewLifecycleOwner) {
            if (it != null) {
                val newView =
                    LayoutInflater.from(context).inflate(R.layout.image_view, container, false)
                val image = newView.findViewById<ImageView>(R.id.productImage)
                image.setImageBitmap(it)
                if (parentCategoryImageClicked) {
                    parentCategoryImage = it
                    isCategoryImageAdded = true
                    addParentImage.setImageBitmap(it)
                    parentCategoryImageClicked = false
                } else {
                    imageList.putIfAbsent(count, IntWithCheckedData(it, false, fileName))
                    val currentCount = count
                    if (mainImageClicked) {
                        mainImageBitmap = it
                        newView.findViewById<CheckBox>(R.id.mainImageCheckBox).isChecked =
                            mainImageClicked
                        imageList[currentCount] = IntWithCheckedData(it, true, fileName)
                        mainImageClicked = false
                    }
                    newView.findViewById<CheckBox>(R.id.mainImageCheckBox)
                        .setOnCheckedChangeListener { buttonView, isChecked ->
                            if (imageList[currentCount] != null) {
                                imageList[currentCount]!!.isChecked = isChecked
                            }
                        }

                    newView.findViewById<ImageButton>(R.id.deleteImage).setOnClickListener {
                        if (imageLoader.deleteImageInApp(
                                requireContext(),
                                imageList[currentCount]?.fileName ?: ""
                            )
                        ) {
                            deletedImageList.add(imageList[currentCount]?.fileName ?: "")
                        }
                        imageLayout.removeView(newView)
                        imageList.remove(currentCount)
                    }
                    imageLayout.addView(newView, 0)
                    count++
                }
            }
        }
    }

    private fun setUpAddNewImageListeners() {
        view.findViewById<ImageView>(R.id.addNewImage).setOnClickListener {
            imagePermissionHandler.checkPermission(true)
        }
        view.findViewById<MaterialButton>(R.id.addNewImageButton).setOnClickListener {
            imagePermissionHandler.checkPermission(true)
        }
    }

    private fun setUpUpdateButtonListener() {
        updateBtn.setOnClickListener {
            errorCheck()
            if(isAnyError()){
                var checkedCount = 0
                var bitmap:Bitmap? = null
                for(i in imageList){
                    if(i.value.isChecked){
                        checkedCount++
                        addEditProductViewModel.deleteImage(i.value.fileName)
                        bitmap = i.value.bitmap
                        mainImageBitmap = bitmap
                    }
                }
                if(checkedCount>1){
                    ShowShortSnackBar.showRedColor(view,"Product Should Contain Only One Main Image")
                }
                else if(checkedCount <= 0){
                    ShowShortSnackBar.showRedColor(view,"Product Should Contain atLeast One Main Image")
                }
                else if(checkedCount==1) {
                    mainImage = "${System.currentTimeMillis()}"
                    imageLoader.storeImageInApp(requireContext(), bitmap!!, mainImage)
                }
                if(mainImage.isNotEmpty() && checkedCount ==1) {
                    val imageListNames = mutableListOf<String>()
                    for (i in imageList) {
                        if ((i.value.bitmap != mainImageBitmap) && (i.value.fileName !in imageStringList)) {
                            val tmpName = System.currentTimeMillis().toString()
                            imageLoader.storeImageInApp(requireContext(), i.value.bitmap, tmpName)
                            imageListNames.add(tmpName)
                        }
                    }
                    val brandNameStr = brandName.text.toString()
                    val subCategoryName = productSubCat.text.toString()
                    if (isNewParentCategory) {
                        val filName = "${System.currentTimeMillis()}"
                        if (parentCategoryImage != null) {
                            imageLoader.storeImageInApp(
                                requireContext(),
                                parentCategoryImage!!,
                                filName
                            )
                        }
                        if (imageLoader.getImageInApp(requireContext(), filName) == null) {
                            isCategoryImageAdded = false
                        }
                        addEditProductViewModel.addParentCategory(ParentCategory(productParentCategory.text.toString(), filName,view.findViewById<TextInputEditText>(R.id.newParentCategoryDescriptionText).text.toString(), false))
                    }
                    if (isNewSubCategory) {
                        addEditProductViewModel.addSubCategory(Category(productSubCat.text.toString(), productParentCategory.text.toString(), ""))
                    }
                    if (isCategoryImageAdded) {
                        val availableItems =productAvailableItems.text.toString().toInt()
                        if(availableItems>=oldAvailableItems) {
                            addEditProductViewModel.updateInventory(
                                brandNameStr,
                                (ProductListFragment.selectedProductEntity.value == null),
                                Product(
                                    0,
                                    0,
                                    subCategoryName,
                                    productName.text.toString(),
                                    productDescription.text.toString(),
                                    productPrice.text.toString().toFloat(),
                                    productOffer.text.toString().toFloat(),
                                    productQuantity.text.toString(),
                                    mainImage,
                                    isVeg.isChecked,
                                    rawManufactureDate,
                                    rawExpiryDate,
                                    productAvailableItems.text.toString().toInt()
                                ),
                                ProductListFragment.selectedProductEntity.value?.productId,
                                imageListNames,
                                deletedImageList,
                                oldMainImage
                            )
                            parentFragmentManager.popBackStack()
                            if (ProductListFragment.selectedProductEntity.value == null) {
                                Toast.makeText(
                                    context,
                                    "Product Added Successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Product Updated Successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                        else{
                            Toast.makeText(
                                context,
                                "Available Items Should be maximum or equal to old Available Items ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                    else{
                        Toast.makeText(context, "Please add the Category Image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(context,"All the Fields are mandatory",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isAnyError(): Boolean {
        return (productNameLayout.error == null &&
                brandNameLayout.error == null&&
                parentCategoryLayout.error == null&&
                subCategoryLayout.error == null&&
                productDescriptionLayout.error == null &&
                priceLayout.error == null&&
                offerLayout.error == null&&
                productQuantityLayout.error == null&&
                availableItemsLayout.error == null&&
                manufactureDateLayout.error == null&&
                (expiryDateLayout.error == null || !expiryDateLayout.isVisible))
    }

    private fun errorCheck() {
        productNameLayout.error = inputChecker.nameCheck(productName)
        brandNameLayout.error = inputChecker.emptyCheck(brandName)
        parentCategoryLayout.error = inputChecker.lengthAndEmptyCheck("Parent Category",productParentCategory,3)
        subCategoryLayout.error = inputChecker.lengthAndEmptyCheck("Sub Category",productParentCategory,3)
        productDescriptionLayout.error = inputChecker.lengthAndEmptyCheck("Product Description",productDescription,30)
        priceLayout.error = inputChecker.emptyCheck(productPrice)
        offerLayout.error = inputChecker.emptyCheck(productOffer)
        productQuantityLayout.error = inputChecker.lengthAndEmptyCheck("Product Quantity",productQuantity,2)
        availableItemsLayout.error = inputChecker.emptyCheck(productAvailableItems)
        manufactureDateLayout.error = inputChecker.emptyCheck(productManufactureDate)
        expiryDateLayout.error = inputChecker.emptyCheck(productExpiryDate)
    }

    private fun setUpTextChangedListeners() {
        productParentCategory.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotEmpty()) {
                    addParentCategoryLayout.visibility = View.VISIBLE
//                    view.findViewById<TextInputLayout>(R.id.newParentCategoryDescription).visibility = View.VISIBLE
                }
                else{
//                    view.findViewById<TextInputLayout>(R.id.newParentCategoryDescription).visibility = View.GONE
                    addParentCategoryLayout.visibility = View.GONE
                }
                if(!addEditProductViewModel.parentCategoryChecker(s.toString(),parentArray)){
                    isNewParentCategory = true
                    view.findViewById<TextInputLayout>(R.id.newParentCategoryDescription).visibility = View.VISIBLE
                    addParentImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.add_photo_alternate_24px))
                    addParentCategoryButton.text = "Add Category Image"
                }
                else{
                    view.findViewById<TextInputLayout>(R.id.newParentCategoryDescription).visibility = View.GONE
                    addEditProductViewModel.getParentCategoryImageForParent(s.toString())
                    addEditProductViewModel.getChildArray(s.toString())
                    isNewParentCategory = false
                }
            }
        })

        productSubCat.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if(!addEditProductViewModel.subCategoryChecker(s.toString(),childArray)){
                    isNewSubCategory = true
                }
                else{
                    isNewSubCategory = false
                }
            }
        })
    }

    private fun setUpDatePickerListeners(dateManufacturePicker: MaterialDatePicker<Long>, dateExpiryPicker: MaterialDatePicker<Long>) {
        productManufactureDate.setOnClickListener {
            dateManufacturePicker.show(parentFragmentManager,"Manufacture Date")
        }
        productExpiryDate.setOnClickListener {
            dateExpiryPicker.show(parentFragmentManager,"Expiry Date")
        }
    }

    private fun setUpCategoryListeners() {
        addParentCategoryButton.setOnClickListener {
            isNewParentCategory = true
            parentCategoryImageClicked = true
            imagePermissionHandler.checkPermission(false)
        }
        addParentImage.setOnClickListener {
            isNewParentCategory = true
            parentCategoryImageClicked = true
            imagePermissionHandler.checkPermission(false)
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
        if(imageList.isEmpty() && productName.text.toString().isEmpty() && brandName.text.toString().isEmpty()
            && productParentCategory.text.toString().isEmpty() && productSubCat.text.toString().isEmpty() && productDescription.text.toString().isEmpty()
            && productPrice.text.toString().isEmpty() && productQuantity.text.toString().isEmpty() && productAvailableItems.text.toString().isEmpty()
            && productManufactureDate.text.toString().isEmpty() && productExpiryDate.text.toString().isEmpty()){
            parentFragmentManager.popBackStack()
        }
        else{
            DataLossAlertDialog().showDataLossAlertDialog(requireContext(),parentFragmentManager)
        }
    }

}