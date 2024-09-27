package com.hubwallet.customer_checkin.ui.book_appointment.service_selection

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.common.extensions.observe
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.FragmentServiceSelectionBinding
import com.hubwallet.customer_checkin.ui.MainActivity
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.adapter.ServiceAdapter
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.adapter.ServiceCategoryAdapter
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.ServiceCategoryModel
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.servicelist.ServiceListModel
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.servicelist.ServiceListResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ServiceSelectionFragment :
    BaseFragment<FragmentServiceSelectionBinding>(R.layout.fragment_service_selection) {
    var serviceListId = ""
    val serviceCategoryAdapter by lazy { ServiceCategoryAdapter(serviceCategoryId) }
    val serviceAdapter by lazy { ServiceAdapter() }

    @Inject
    lateinit var preferenceManager: PreferenceManager

    val serviceCategoryId: (list: List<String>) -> Unit = {
        Log.e("ServiceCategoryId", it.toString())
        serviceAdapter.filterList(it)
    }


    private val viewModel by viewModels<ServiceSelectionFragmentViewModel>()

    override fun onInitDataBinding() {
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(requireActivity())  {
//
//        }
        viewBinding.swipeRefresh.setOnRefreshListener {

            viewModel.serviceCategory( preferenceManager.getValueString(PreferenceManager.vendorKey))
            viewModel.serviceListByCategoryId( preferenceManager.getValueString(PreferenceManager.vendorKey), "")
        }
        viewBinding.btCancel.setOnClickListener {
            findNavController().navigate(R.id.homeScreen)
        }
        (activity as MainActivity).backButtonShow(false)
        Log.e("onInitDataBinding124 ", "000000000000000")
        viewModel.serviceCategory( preferenceManager.getValueString(PreferenceManager.vendorKey))
        viewModel.serviceListByCategoryId( preferenceManager.getValueString(PreferenceManager.vendorKey), "")
        viewBinding.apply {
            recyclerViewCategory.adapter = serviceCategoryAdapter

            viewBinding.searchView.addTextChangedListener { e ->
                serviceAdapter.filterListBySearch(e.toString())
            }

//            recyclerView.adapter = ConcatAdapter( serviceCategoryAdapter, serviceAdapter)
            btNext.setOnClickListener {
                var duration = 0
                var durationList = mutableListOf<String>()
                val listf = serviceAdapter.serviceList.filter { it.isSelected }
                listf.forEach {
                    Log.e("onInitDataBingDuration ", it.service_duration?.toInt().toString())
                    duration += it.service_duration?.toInt() ?: 0
                    durationList.add(it.service_duration.toString())
                }
                val list = listf.map { it.service_id }
                val prices = listf.map { it.service_price }
                val serviceName = listf.map { it.service_name }
                val durations = durationList.joinToString(",")
                val services = TextUtils.join(",", list)
                val ps = TextUtils.join(",", prices)
                val names = TextUtils.join(",", serviceName)
                if (services.isEmpty()) {
                    Toast.makeText(requireContext(), "Please Select Services!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                val bundle = Bundle()
                bundle.putString("key", names)
                bundle.putString("services", services)
                bundle.putString("time", "")
                bundle.putString("date", "")
                bundle.putString("duration", duration.toString())
                bundle.putString("durations", durations)
                bundle.putString("prices", ps)
                bundle.putString("services_name", names)


                findNavController().navigate(R.id.providerSelectionFragment, bundle)
            }
        }
        observe(viewModel.serviceCategoryLiveData, ::serviceCategoryObserver)
        observe(viewModel.serviceListByCategoryId, ::observeService)
    }

    private fun observeService(response: NetworkResponse<ServiceListModel>) {
        when (response) {
            is NetworkResponse.Loading -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)

            }
            is NetworkResponse.Success -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                if (response.data.status == 1) {
                    viewBinding.recyclerViewService.adapter = serviceAdapter
                    serviceAdapter.updateList(response.data.serviceListResult as ArrayList<ServiceListResult>)
                    Log.e("serviCatByIdObserver: ", response.toString())
                }


            }
            is NetworkResponse.Failed -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun serviceCategoryObserver(data: NetworkResponse<ServiceCategoryModel>) {
        when (data) {
            is NetworkResponse.Loading -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)
            }
            is NetworkResponse.Success -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                Log.e("serviceCategoryObserver", data.data.toString())
                viewBinding.recyclerViewCategory.adapter = serviceCategoryAdapter
                serviceCategoryAdapter.updateList(data.data.result)

            }
            is NetworkResponse.Failed -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                Toast.makeText(requireContext(), data.error, Toast.LENGTH_SHORT).show()

            }
        }

    }

}