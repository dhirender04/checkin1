package com.hubwallet.customer_checkin.ui.existing_customer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.common.extensions.observe
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.CheckinDialogBoxLayoutBinding
import com.hubwallet.customer_checkin.databinding.FragmentExistingCustomerCheckInBinding
import com.hubwallet.customer_checkin.ui.MainActivity
import com.hubwallet.customer_checkin.ui.existing_customer.network.model.ExistingCustomerModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExistingCustomerCheckInFragment :
    BaseFragment<FragmentExistingCustomerCheckInBinding>(R.layout.fragment_existing_customer_check_in) {
    private val viewModel: ExistingCustomerCheckInViewModel by viewModels()
    private var registerCustomerDialog: AlertDialog? = null
    @Inject
    lateinit var preferenceManager: PreferenceManager
    override fun onInitDataBinding() {
        Log.e("onInitDataBinding: ", "0000000000")
        (activity as MainActivity).bottomPowerByIsShow(true)
        (activity as MainActivity).topPowerByIsShow(true)
        (activity as MainActivity).backButtonShow(true)
        viewBinding.viewModel = this.viewModel
        (activity as MainActivity).backButtonVisiable(true)

        viewBinding.apply {
            btOkay.setOnClickListener {
                Log.e("onInitDataBinding0101 ", keyInput.text.toString())
                if (isValidate()) {
                    viewModel?.existingCustomerCheckIn(
                        keyInput.text.toString(),
                        preferenceManager.getValueString(PreferenceManager.vendorKey),
                        "0"
                    )
                }
            }
            submit1.setOnClickListener {
                if (isValidate()) {
                    viewModel?.existingCustomerCheckIn(
                        keyInput.text.toString(),
                        preferenceManager.getValueString(PreferenceManager.vendorKey), "0"
                    )
                }
            }
        }
        observe(viewModel.pinLiveData, ::observePin)
        observe(viewModel.existingCustomerMobileNoLiveData, ::existingCustomerMobileNoObserver)
    }

    private fun existingCustomerMobileNoObserver(response: NetworkResponse<ExistingCustomerModel>) {
        when (response) {
            is NetworkResponse.Loading -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)
            }
            is NetworkResponse.Success -> {
                Log.e("existingCus101010 ", response.toString())
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                if (response.data.status == 4) {
                    findNavController().navigate(R.id.serviceSelectionFragment)
                    preferenceManager.putValueString(PreferenceManager.customerId, response.data.customer_id.toString())

                    Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_SHORT)
                        .show()
                } else  if (response.data.status == 3) {
                    Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_SHORT)
                        .show()
                } else  if (response.data.status == 0) {
                    Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_SHORT)
                        .show()
                }
                viewBinding.keyInput.setText("")
                viewModel.builder.clear()
            }
            is NetworkResponse.Failed -> {
                viewBinding.keyInput.setText("")
                viewModel.builder.clear()
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()


            }
        }

    }

    private fun isValidate(): Boolean {
        if (viewBinding.keyInput.text.toString().isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please enter phone number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (viewBinding.keyInput.text?.length!! < 14) {
            Toast.makeText(requireContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    fun observePin(al: String) {

        viewBinding.keyInput.setText(al)
    }

    private fun registerCustomerDialogBox(msg: String, customerId: String?) {
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
            if (!customerId.isNullOrEmpty()) {
                okButton.text = getString(R.string.book_appointment)
            } else {
                okButton.text = "Okay"
            }
            checkInText.text = msg

            okButton.setOnClickListener {
                if (okButton.text.equals(getString(R.string.book_appointment))) {
                    findNavController().navigate(R.id.serviceSelectionFragment)
                } else {
                    findNavController().navigate(R.id.homeScreen)
                }
                registerCustomerDialog?.dismiss()
            }
        }
        registerCustomerDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        registerCustomerDialog?.show()


    }

}