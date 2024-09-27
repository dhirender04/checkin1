package com.hubwallet.customer_checkin.common.base

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.DialogPin2Binding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LogoutPop(val findNavController: NavController,val response: BaseResponse?) :
    BaseDailogCenterFragment<DialogPin2Binding>(R.layout.dialog_pin2) {
    @Inject
    lateinit var manager: PreferenceManager

    override fun onInitDataBinding() {


        if (response?.status?.equals(1) == true){
            viewBinding.ivGreenTick.setVisibility(true)
        }else{
            viewBinding.ivGreenTick.setVisibility(false)
        }
        viewBinding.pinEditText.text = response?.message ?: ""
        viewBinding.btnContinue.setOnClickListener {
            findNavController.navigate(R.id.homeScreen)
        }
        viewBinding.btnCancel.setOnClickListener {
            dismiss()
        }
    }


}