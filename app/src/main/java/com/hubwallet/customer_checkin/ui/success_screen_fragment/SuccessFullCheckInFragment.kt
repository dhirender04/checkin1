package com.hubwallet.customer_checkin.ui.success_screen_fragment

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.databinding.FragmentSuccessFullCheckInBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SuccessFullCheckInFragment :
    BaseFragment<FragmentSuccessFullCheckInBinding>(R.layout.fragment_success_full_check_in) {
    override fun onInitDataBinding() {
        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000)
            activity?.runOnUiThread {
                findNavController().navigate(R.id.action_successFullCheckInFragment_to_homeFragment)
            }
        }
    }


}