package com.hubwallet.customer_checkin.ui.cancelation

import androidx.fragment.app.viewModels
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.base.BaseDailogCenterFragment
import com.hubwallet.customer_checkin.common.extensions.observe
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.FragmentCancellationBinding
import com.hubwallet.customer_checkin.ui.cancelation.network.model.CancellationModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CancellationDialog :
    BaseDailogCenterFragment<FragmentCancellationBinding>(
        R.layout.fragment_cancellation
    ) {
    val viewModel: CancellationViewModel by viewModels()

    @Inject
    lateinit var preferenceManager: PreferenceManager
    override fun onInitDataBinding() {
        viewBinding.scroll.setVisibility(true)
         viewModel.cancellationApi( preferenceManager.getValueString(PreferenceManager.vendorKey))
        observe(viewModel.cancellationLiveData, ::cancellationObserver)

        viewBinding.close.setOnClickListener {
            dismiss()
        }

    }

    private fun cancellationObserver(data: NetworkResponse<CancellationModel>) {
        when (data) {
            is NetworkResponse.Loading -> {
                viewBinding.progress.setVisibility(true)


            }
            is NetworkResponse.Success -> {
                viewBinding.progress.setVisibility(false)
                if (data.data.status==1){
                    if (!data.data.result.isNullOrEmpty()){
                        viewBinding.text.text = data.data.result[0].description
                    }
                }

            }
            is NetworkResponse.Failed -> {
                viewBinding.progress.setVisibility(false)

            }
        }


    }
}