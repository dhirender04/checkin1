package com.hubwallet.customer_checkin.ui.book_appointment.checkout

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.braintreepayments.cardform.utils.CardType
import com.braintreepayments.cardform.view.CardEditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.common.extensions.observe
import com.hubwallet.customer_checkin.common.extensions.roundTwoDecimalNo
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.common.utils.DateTimeUtils
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.AddNewCardPopUpBinding
import com.hubwallet.customer_checkin.databinding.AppointmentBookDialogBoxBinding
import com.hubwallet.customer_checkin.databinding.FragmentCheckoutBinding
import com.hubwallet.customer_checkin.ui.MainActivity
import com.hubwallet.customer_checkin.ui.book_appointment.checkout.adapter.CheckoutAdapter
import com.hubwallet.customer_checkin.ui.book_appointment.checkout.network.card_model.CardResponse
import com.hubwallet.customer_checkin.ui.cancelation.CancellationDialog
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding>(R.layout.fragment_checkout) {
    val checkoutAdapter by lazy { CheckoutAdapter() }
    val viewModel by viewModels<CheckoutFragmentViewModel>()
    var appointmentDialog: AlertDialog? = null
    private lateinit var dialogAddCard: androidx.appcompat.app.AlertDialog
    private lateinit var bind: AddNewCardPopUpBinding
    private var isCardValid = true

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onInitDataBinding() {
        try {
            Log.e("onInitDaee333333g: ", requireArguments().getString("time").toString())
            val date =
                DateTimeUtils.normalDateFormatter.parse(requireArguments().getString("date", ""))
            val time = DateTimeUtils.timeFormatter24.parse(requireArguments().getString("time"))
            val dateString = DateTimeUtils.summaryDateFormat.format(date)
            val time2 = DateTimeUtils.workingHourFormat.format(time)
            val list = requireArguments().getString("time")?.split(",")
            var timeSho = list?.get(list.size - 1)
            list?.forEach {
                val times = it.replace(":", "")
                if (it != timeSho) {
                    if (times.toInt() < (timeSho?.replace(":", "")?.toInt() ?: 0)) {
                        timeSho = it
                    }
                }
            }

            viewBinding.tvDateTime.text = "$dateString at ${
                DateTimeUtils.timeFormatter12a.format(
                    DateTimeUtils.timeFormatter24.parse(timeSho)
                )
            }"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val services = requireArguments().getString("services_name", "").split(",")
        val names = requireArguments().getString("provider_name", "").split(",")
        val prices = requireArguments().getString("prices", "").split(",")
        var totalPrice = 0f
        prices.forEach {

            totalPrice += it.toFloat()
        }
        viewBinding.tvDurationTime.text = "${requireArguments().getString("duration", "0")} Min"
        checkoutAdapter.updateList(services, names, prices)
        viewBinding.total.text = "$${totalPrice.roundTwoDecimalNo().toString()}"


        viewBinding.apply {
            rvOrderData.adapter = checkoutAdapter
            cancel.setOnClickListener {
                CancellationDialog().show(childFragmentManager, "cancel")
            }
            btBookAppointment.setOnClickListener {
                if (checkbox.isChecked) {
                    addAppointment()
//                    viewModel.getCardList(
//                        preferenceManager.getValueString(PreferenceManager.customerId),
//                        preferenceManager.getValueString(PreferenceManager.vendorKey)
//                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please accept cancellation & no show policy !",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            btBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        observe(viewModel.getAddAppointmentLiveData, ::observeAddApointment)
        observe(viewModel.getcardLiveData, ::observeCard)
        observe(viewModel.getAddCardLiveData, ::observeAddCard)
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
                            addAppointment()
                        }else{
                            openPopupCard()
                        }
                    }else{
                        addAppointment()
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
    private fun observeAddCard(data: NetworkResponse<BaseResponse>) {
        when (data) {
            is NetworkResponse.Failed -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                bind.progress.setVisibility(false)
            }
            is NetworkResponse.Success -> {
                bind.progress.setVisibility(false)
                if (data.data.status == 1) {
                    addAppointment()
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

    private fun observeAddApointment(data: NetworkResponse<BaseResponse>) {
        when (data) {
            is NetworkResponse.Loading -> {
                viewBinding.btBookAppointment.isEnabled = false
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)

            }
            is NetworkResponse.Success -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                if (data.data.status == 1) {
                    appointmentBookDialog(data.data.message)
                }

            }
            is NetworkResponse.Failed -> {
                viewBinding.btBookAppointment.isEnabled = true
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)

            }
        }
    }

    private fun addAppointment() {
//        Toast.makeText(requireContext(), preferenceManager.getValueString(PreferenceManager.customerId), Toast.LENGTH_SHORT).show()
        val jsonObject = JSONObject()
        jsonObject.put("vendor_id",  preferenceManager.getValueString(PreferenceManager.vendorKey))
        jsonObject.put("customer_id", preferenceManager.getValueString(PreferenceManager.customerId))
        jsonObject.put(
            "stylist_id",
            arguments?.getString("service_provider", "")?.replace(" ", "") ?: ""
        )
        jsonObject.put("service", arguments?.getString("services", "") ?: "")
        jsonObject.put("date", requireArguments().getString("date", ""))
        jsonObject.put("start_time", requireArguments().getString("time", ""))
        jsonObject.put("end_time", requireArguments().getString("end_time", ""))
        jsonObject.put("booking_flow", requireArguments().getString("flow", ""))
        jsonObject.put("total_duration", requireArguments().getString("duration", "0"))
        jsonObject.put("customer_note", "")
        viewModel.addAppointment(jsonObject.toString())
    }

    private fun appointmentBookDialog(message: String) {
        val appointmentBookDialogBoxBinding =
            DataBindingUtil.inflate<AppointmentBookDialogBoxBinding>(
                layoutInflater,
                R.layout.appointment_book_dialog_box,
                null,
                false
            )
        appointmentDialog =
            MaterialAlertDialogBuilder(requireContext()).setView(appointmentBookDialogBoxBinding.root)
                .setCancelable(false).show()
        appointmentBookDialogBoxBinding.apply {
            tvSuccessText.text = message
            btOkay.setOnClickListener {
//                childFragmentManager.beginTransaction().remove(this@CheckoutFragment)
                (activity as MainActivity).checkIfCheckoutContainerActive(true)
//                findNavController().popBackStack()
                findNavController().navigate(R.id.homeScreen)
                appointmentDialog?.dismiss()
            }
        }
        appointmentDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        appointmentDialog?.show()
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

            val jsonObject = JSONObject()
            jsonObject.put("customer_id",preferenceManager.getValueString(PreferenceManager.customerId))
            jsonObject.put("card_holder_name",bind.cardHolderName.text.toString())
            jsonObject.put("card_number",bind.cardNumber.text.toString())
            jsonObject.put("cvv",bind.cvv.text.toString())
            jsonObject.put("expiry_month",month)
            jsonObject.put("expiry_year",year)
            jsonObject.put("card_type","")
            jsonObject.put("zipcode",bind.zip.text.toString() ?: "")
            viewModel.addCard(jsonObject.toString())
        }
        bind.clos.setOnClickListener { dialogAddCard.dismiss() }


    }

}