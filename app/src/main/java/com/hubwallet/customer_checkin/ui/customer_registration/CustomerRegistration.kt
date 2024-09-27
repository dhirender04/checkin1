package com.hubwallet.customer_checkin.ui.customer_registration

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.braintreepayments.cardform.utils.CardType
import com.braintreepayments.cardform.view.CardEditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.R.array.gender
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.common.base.Keys
import com.hubwallet.customer_checkin.common.base.Keys.emailPattern
import com.hubwallet.customer_checkin.common.extensions.observe
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.common.utils.DateTimeUtils.dateFormatter2_
import com.hubwallet.customer_checkin.common.utils.DateTimeUtils.dateFormatter_2
import com.hubwallet.customer_checkin.common.utils.DateTimeUtils.normalDateFormatter
import com.hubwallet.customer_checkin.common.utils.DateTimeUtils.normalDateFormatter_2
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.AddNewCardPopUpBinding
import com.hubwallet.customer_checkin.databinding.CheckinDialogBoxLayoutBinding
import com.hubwallet.customer_checkin.databinding.CustomerRegistrationBinding
import com.hubwallet.customer_checkin.ui.MainActivity
import com.hubwallet.customer_checkin.ui.book_appointment.checkout.network.card_model.CardResponse
import com.hubwallet.customer_checkin.ui.customer_registration.network.model.CustomerRegistrationModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CustomerRegistration :
    BaseFragment<CustomerRegistrationBinding>(R.layout.customer_registration) {
    private val viewModel by viewModels<CustomerRegistrationViewModel>()
    private var genderSelect = ""
    private var birthDate = ""
    private var isBirthDay = false
    private lateinit var bind: AddNewCardPopUpBinding
    private var isCardValid = true
    private lateinit var dialogAddCard: androidx.appcompat.app.AlertDialog


    @Inject
    lateinit var preferenceManager: PreferenceManager

    private var registerCustomerDialog: AlertDialog? = null

    private val genderAdapter by lazy {
        val genderList = resources.getStringArray(gender)
        ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, genderList)
    }

    override fun onInitDataBinding() {
        Keys.timePicker()
        Keys.dataPicker()
        (activity as MainActivity).bottomPowerByIsShow(true)
        (activity as MainActivity).topPowerByIsShow(true)
        (activity as MainActivity).backButtonShow(true)
        observe(viewModel.customerRegistrationLiveData, ::customerRegistrationObserver)
        observe(viewModel.getcardLiveData, ::observeCard)
        observe(viewModel.getAddCardLiveData, ::observeAddCard)

        (activity as MainActivity).backButtonVisiable(true)

        viewBinding.apply {
            spGender.setAdapter(genderAdapter)
            tvFirstName.addTextChangedListener {
                tvFirstNameWrap.error = null
            }
            tvLastName.addTextChangedListener {
                tvLastNameWrap.error = null
            }
            tvMobile.addTextChangedListener {
                tvMobileWrap.error = null
            }
            tvEmail.addTextChangedListener {
                tvEmailWrap.error = null
            }

            tvMobile.setText("")
            tvBirthday.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(its: Editable?) {
                    try {
                        if (its.toString().length >= 2 && its.toString().substring(0, 2) == "00") {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        } else {
                            tvBirthday.error = null
                        }
                        if (its.toString().length == 4) {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        }
                        if (its.toString().length >= 2 && its.toString().substring(0, 2)
                                .replace("-", " ")
                                .toInt() > 12
                        ) {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        } else {
                            tvBirthday.error = null
                        }
                        val calendar = Calendar.getInstance().get(Calendar.YEAR).toString()
                        // val month = Calendar.getInstance().get(Calendar.MONTH)
                        val mycal: Calendar = GregorianCalendar(Calendar.YEAR, Calendar.MONTH, 1)
                        val daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH)
                        Log.e(
                            "TAG",
                            "afterTextChanged:year.. ${
                                calendar.substring(1, 4).toInt()
                            }..$calendar..$daysInMonth"
                        )

                        if (its.toString().length == 5 && its.toString().substring(3, 5)
                                .replace("-", "")
                                .toInt() > daysInMonth.toInt()
                        ) {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        }
                        if (its.toString().length == 5 && its.toString().substring(3, 5)
                                .replace("-", "")
                                .toInt() == calendar.substring(2, 4).toInt() && its.toString()
                                .substring(0, 2)
                                .toInt() > daysInMonth.toInt()
                        ) {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        }
                        if (its.toString().length == 5 && its.toString().substring(3, 5)
                                .replace("-", "")
                            == "00"
                        ) {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        }

                        if (its.toString().length == 5 && its.toString().substring(3, 5)
                                .replace("-", "")
                                .toInt() > daysInMonth.toInt()
                        ) {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        } else {
                            tvBirthday.error = null
                        }

                        if (its.toString().length == 10 && its.toString().substring(3, 5)
                                .replace("-", "")
                                .toInt() > daysInMonth.toInt()
                        ) {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        }
                        if (its.toString().length == 10 && its.toString().substring(6, 10)
                                .replace("-", "")
                                .toInt() >= calendar.toInt()
                        ) {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        }
                        if (its.toString().length == 10 && its.toString().substring(6, 10)
                                .replace("-", "")
                            == "0000"
                        ) {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        } else {
                            if (its.toString().length == 10 && its.toString().substring(3, 5)
                                    .replace("-", "")
                                    .toInt() > daysInMonth.toInt()
                            ) {
                                tvBirthday.error = "Invalid Date"
                                isBirthDay = true
                                return
                            } else {
                                tvBirthday.error = null
                                if (tvBirthday.text.toString().length == 10) {
                                    birthDate =
                                        normalDateFormatter.format(dateFormatter2_.parse("${tvBirthday.text}"))
                                    isBirthDay = false
                                }
                            }
                        }
                        if (its.toString().length < 9
                        ) {
                            tvBirthday.error = "Invalid Date"
                            isBirthDay = true
                            return
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })




            btnRegister.setOnClickListener {
//                registerCustomerDialogBox("CHECK DE")
                if (isValidation()) {
//                    viewModel.getCardList(
//                        preferenceManager.getValueString(PreferenceManager.customerId),
//                        preferenceManager.getValueString(PreferenceManager.vendorKey)
//                    )
                    addCustomer()
                }

            }

            spGender.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    genderSelect = parent.getItemAtPosition(position).toString()
                }


        }
    }

    private fun addCustomer(){

        val firstName = viewBinding.tvFirstName.text.toString()
        val lastName = viewBinding.tvLastName.text.toString()
        val mobileNumber = viewBinding.tvMobile.text.toString()
        val email = viewBinding.tvEmail.text.toString()
        var dob: String? = ""
        try {
            dob =
                normalDateFormatter.format(dateFormatter2_.parse("${viewBinding.tvBirthday.text}"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

//                    val dob = if (tvBirthday.text.toString().isNotEmpty()
//                    ) tvBirthday.text.toString() else ""

        val newCustomerRegistrationJson = JSONObject()
        newCustomerRegistrationJson.put("firstname", firstName)
        newCustomerRegistrationJson.put("lastname", lastName)
        newCustomerRegistrationJson.put("mobile", mobileNumber)
        newCustomerRegistrationJson.put("email", email)
        newCustomerRegistrationJson.put("gender", genderSelect)
        newCustomerRegistrationJson.put("birth_date", dob ?: "")
        newCustomerRegistrationJson.put(
            "vendor_id",
            preferenceManager.getValueString(PreferenceManager.vendorKey)
        )
        val mainRegistrationJson = JSONObject()
        mainRegistrationJson.put(
            "new_customer_registration",
            newCustomerRegistrationJson
        )
        Log.e("customerRegistratJson", mainRegistrationJson.toString())

        viewModel.customerRegistration(mainRegistrationJson.toString())
    }

    private fun datePickerListener(): String {
        var date = ""
        var month = ""
        var day = ""
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view1: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->

                if ((monthOfYear + 1) < 10) month = "0" + (monthOfYear + 1)
                else month = "" + (monthOfYear + 1)
                if ((dayOfMonth) < 10) day = "0" + (dayOfMonth)
                else day = "" + (dayOfMonth)

                date = month + "-" + day + "-" + year.toString()
                var date111 = month + "/" + day + "/" + year.toString()
                date = "${dateFormatter_2.format(dateFormatter_2.parse(date111))}"
                // date = "${normalDateFormatter_2.format(dateFormatter_2.parse(date))}"

                if (isBirthDay) {
//                    birthDate =  year.toString() + "-" + month + "-" + day
                    birthDate = "${normalDateFormatter_2.format(dateFormatter_2.parse(date))}"
                    viewBinding.tvBirthday.setText(date)
                }


            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()

        return date
    }

    private fun observeAddCard(data: NetworkResponse<BaseResponse>) {
        when (data) {
            is NetworkResponse.Failed -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                bind.progress.setVisibility(false)
            }
            is NetworkResponse.Success -> {
                bind.progress.setVisibility(false)
                if (data.data.status == 1) {
                    addCustomer()
                    dialogAddCard.dismiss()
                    bind.addButton.isEnabled = false

                    Toast.makeText(requireContext(), data.data.message, Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(requireContext(), data.data.message, Toast.LENGTH_SHORT).show()
                }
            }
            is NetworkResponse.Loading -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)

            }

        }
    }
    private fun observeCard(data: NetworkResponse<CardResponse>) {
        when (data) {
            is NetworkResponse.Failed -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)

            }
            is NetworkResponse.Success -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                if (data.data.status == 1) {


                    if (preferenceManager.getValueString(PreferenceManager.is_card_booking_available).equals("1")){
                        if (data.data.result?.size!! > 0){
                            addCustomer()
                        }else{
                            openPopupCard()
                        }
                    }else{
                        addCustomer()
                    }

                } else {
                    Toast.makeText(requireContext(), data.data.message, Toast.LENGTH_SHORT).show()
                }
            }
            is NetworkResponse.Loading -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)

            }

        }
    }
    private fun customerRegistrationObserver(data: NetworkResponse<CustomerRegistrationModel>) {
        when (data) {
            is NetworkResponse.Loading -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)

            }
            is NetworkResponse.Success -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                if (data.data.status == 1) {
                    Log.e("CustomerRegistration: ", data.data.toString())
                    preferenceManager.putValueString(
                        PreferenceManager.customerId,
                        data.data.customer_id.toString()
                    )
//                    MainActivity.customerId = data.data.customer_id.toString()
                    registerCustomerDialogBox(data.data.message)
                }
                if (data.data.status == 0) {
                    Toast.makeText(requireContext(), data.data.message, Toast.LENGTH_SHORT).show()
                }


            }
            is NetworkResponse.Failed -> {
                Toast.makeText(requireContext(), data.error, Toast.LENGTH_SHORT).show()
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)


            }
        }

    }


    private fun isValidation(): Boolean {
        viewBinding.tvFirstNameWrap.error = null
        viewBinding.tvLastNameWrap.error = null
        viewBinding.tvEmailWrap.error = null
        viewBinding.tvMobileWrap.error = null

        if (viewBinding.tvFirstName.text.toString().isEmpty()) {
            viewBinding.tvFirstNameWrap.error = "Please Enter First Name"
            Toast.makeText(requireContext(), "Please Enter First Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (viewBinding.tvLastName.text.toString().isEmpty()) {
            val r = viewBinding.tvMobile.text?.length
            viewBinding.tvLastNameWrap.error = "Please Enter Last Name"
            Toast.makeText(requireContext(), "Please Enter Last Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (viewBinding.tvMobile.text.toString().isEmpty()) {
            viewBinding.tvMobileWrap.error = "Please Enter Mobile Number"
            Toast.makeText(requireContext(), "Please Enter Mobile Number", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (viewBinding.tvMobile.text?.length!! < 14) {
            viewBinding.tvMobileWrap.error = "Please Enter Valid Mobile Number"
            Toast.makeText(requireContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (viewBinding.tvEmail.text.toString().isEmpty()) {
            viewBinding.tvEmailWrap.error = "Please Enter Email"
            Toast.makeText(requireContext(), "Please Enter Email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!viewBinding.tvEmail.text.toString().trim().matches(emailPattern.toRegex())) {
//        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(viewBinding.tvEmail.text.toString().trim().toString()).matches()) {
            viewBinding.tvEmailWrap.error = "Please Enter valid Email"
            Toast.makeText(requireContext(), "Please Enter valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (isBirthDay) {
            Toast.makeText(
                requireContext(), "Please Enter valid DOB",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun registerCustomerDialogBox(msg: String) {
        val registerDialogBinding = DataBindingUtil.inflate<CheckinDialogBoxLayoutBinding>(
            layoutInflater,
            R.layout.checkin_dialog_box_layout,
            null,
            false
        )
        registerCustomerDialog =
            MaterialAlertDialogBuilder(requireContext()).setView(registerDialogBinding.root)
                .setCancelable(false).show()

        registerDialogBinding.apply {
            checkInText.text = msg
            okButton.text = getString(R.string.book_appointment)
            okButton.setOnClickListener {
                findNavController().navigate(R.id.serviceSelectionFragment)
                registerCustomerDialog?.dismiss()
            }
        }
        registerCustomerDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        registerCustomerDialog?.show()


    }
    private fun openPopupCard() {
        bind = DataBindingUtil.inflate<AddNewCardPopUpBinding>(
            layoutInflater,
            R.layout.add_new_card_pop_up,
            null,
            false
        )
        dialogAddCard = MaterialAlertDialogBuilder(requireContext()).setView(bind.root).create()

        dialogAddCard.show()

        bind.cardNumber.setOnCardTypeChangedListener(object :
            CardEditText.OnCardTypeChangedListener {
            override fun onCardTypeChanged(cardType: CardType?) {
                Log.e("onCardTypeCh", cardType.toString())
                if (cardType.toString().equals("UNKNOWN")) {
                    isCardValid = false
                } else {
                    isCardValid = true
                }
            }

        })


        bind.date.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(it: Editable?) {
                try {
                    if (it.toString().length >= 2 && it.toString().substring(0, 2) == "00") {
                        bind.date.error = "Invalid Month"
                        return
                    } else {
                        bind.date.error = null
                    }
                    if (it.toString().length == 4) {
                        bind.date.error = "Invalid Year"
                        return
                    }
                    if (it.toString().length >= 2 && it.toString().substring(0, 2).toInt() > 12) {
                        bind.date.error = "Invalid Month"
                        return
                    } else {
                        bind.date.error = null
                    }
                    val calendar = Calendar.getInstance().get(Calendar.YEAR).toString()
                    val month = Calendar.getInstance().get(Calendar.MONTH)

                    if (it.toString().length == 5 && it.toString().substring(3, 5)
                            .toInt() < calendar.substring(2, 4).toInt()
                    ) {
                        bind.date.error = "Invalid Year"
                        return
                    } else {
                        bind.date.error = null
                    }
                    if (it.toString().length == 5 && it.toString().substring(3, 5)
                            .toInt() == calendar.substring(2, 4).toInt() && it.toString()
                            .substring(0, 2)
                            .toInt() < month + 1
                    ) {
                        bind.date.error = "Invalid Month"
                        return
                    } else {
                        bind.date.error = null
                    }
                    if (it.toString().length == 5 && it.toString().substring(3, 5)
                        == "00"
                    ) {
                        bind.date.error = "Invalid Year"
                        return
                    } else {
                        bind.date.error = null
                    }

                }catch (e:Exception){
                    bind.date.error=null
                    bind.date.setText("")
                    e.printStackTrace()
                }
            }
        })


        bind.addButton.setOnClickListener {

            var month = ""
            var year = ""
            if (bind.cardHolderName.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Enter Card Holder Name!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
//            if (bind.cardNumber.text?.toString()?.length != 19) {
//                Toast.makeText(requireContext(), "Enter valid Card Number!", Toast.LENGTH_SHORT)
//                    .show()
//                return@setOnClickListener
//            }
            if (bind.cardNumber.text.toString()
                    .isEmpty() && bind.cardNumber.text.toString().length < 15) {
                Toast.makeText(requireContext(), "Enter valid Card Number!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (bind.date.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Enter Expiry  Date!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            if (bind.date.error != null) {
                Toast.makeText(requireContext(), "${bind.date.error}", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            if (bind.zip.text.toString().isNotEmpty() && bind.zip.text.length < 5) {
                Toast.makeText(requireContext(), "Enter Zip Code!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            if (bind.cvv.text.toString().length < 3) {
                Toast.makeText(requireContext(), "Enter Valid Cvv!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            if (!isCardValid) {
                Toast.makeText(requireContext(), "Card Number is not valid!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            month = bind.date.text.toString().split("/")[0]
            year = bind.date.text.toString().split("/")[1]
            if (bind.cvv.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Enter CVV !", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            bind.progress.visibility = View.VISIBLE
            bind.addButton.isEnabled = false
            viewModel.addCard(
                preferenceManager.getValueString(PreferenceManager.customerId),
                bind.cardHolderName.text.toString(),
                bind.cardNumber.text.toString(),
                bind.cvv.text.toString(),
                month,
                year, "", bind.zip.text.toString() ?: ""
            )
        }
        bind.clos.setOnClickListener { dialogAddCard.dismiss() }


    }

}

