package com.hubwallet.customer_checkin.ui.book_appointment.provider_selection

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.common.extensions.observe
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.FragmentProviderSelectionBinding
import com.hubwallet.customer_checkin.ui.MainActivity
import com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.adapter.ProviderNameAdapter
import com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.model.DateProviderServiceModel
import com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.model.ProviderData
import com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.model.ProviderModel
 import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class ProviderSelectionFragment :
    BaseFragment<FragmentProviderSelectionBinding>(R.layout.fragment_provider_selection) {

    val providerNameAdapter by lazy { ProviderNameAdapter(callbackStylistSelection) }
    val viewModel by viewModels<ProviderSelectionViewModel>()
    private val callbackStylistSelection = { str: String, name: String, price: String ->
        if (str == "0") {
            Toast.makeText(requireContext(), "Please select provider !", Toast.LENGTH_SHORT).show()
        } else {
            gotoDateTimeStylist("", str, name, price)
        }
    }

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onInitDataBinding() {
        val c: Date = Calendar.getInstance().getTime()
        println("Current time => $c")

        val df = SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)
        Log.e("onInitDataBinding: ", formattedDate)

        viewBinding.apply {
            btNext.isEnabled = false
            viewModel.providerList(
                preferenceManager.getValueString(PreferenceManager.vendorKey),
                "get_by_stylist",
                arguments?.getString("services").toString(),
                "",
                "",
                ""
            )


            recyclerViewProvider.adapter = providerNameAdapter
            viewBinding.searchView.addTextChangedListener { e ->
                providerNameAdapter.filterListBySearch(e.toString())
            }
            btBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btNext.setOnClickListener {
                providerNameAdapter.getProviderData()
            }
        }

        observe(viewModel.providerListLiveData, ::observerProviderList)

    }

    private fun observerProviderList(response: NetworkResponse<String>) {
        when (response) {
            is NetworkResponse.Loading -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)

            }
            is NetworkResponse.Success -> {
               viewBinding.btNext.isEnabled = true
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                val json = JSONObject(response.data)
                if (json.getInt("status") == 1) {
                    val resultObject = json.getJSONObject("result")
                    val keys = resultObject.keys()
                    val list = mutableListOf<ProviderModel>()
                    var pos = 0
                    keys.forEach {
                        val array = resultObject.getJSONArray(it)
                        val dataList = mutableListOf<ProviderData>()
                        dataList.clear()

                        if (arguments?.getString("key").equals("provider")) {
                            dataList.add(
                                ProviderData(
                                    "Any Available",
                                    "any",
                                    false,
                                    requireArguments().getString("prices")?.split(",")?.get(pos)
                                        ?: "",
                                    "",
                                    ""
                                )

                            )
                        }



                        for (i in 0 until array.length()) {
                            val o = array.getJSONObject(i)
                            val pro = ProviderData(
                                o.getString("stylist_name"),
                                o.getString("stylist_id"),
                                false,
                                o.getString("price"),
                                o.getString("image"),
                                o.getString("note")
                            )
                            dataList.add(pro)
                        }
                        list.add(ProviderModel(it, dataList))
                        pos++
                    }
                    Log.e("observerProviderList: ", list.size.toString())
                    if (MainActivity.isTableModeOn){
                        if (list.size > 1) {
                            viewBinding.recyclerViewProvider.layoutManager =
                                GridLayoutManager(requireContext(), 2)
                        } else {
                            viewBinding.recyclerViewProvider.layoutManager =
                                GridLayoutManager(requireContext(), 1)
                        }
                    }else{
                        viewBinding.recyclerViewProvider.layoutManager =LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    }

                    providerNameAdapter.updateList(list)
                }

            }
            is NetworkResponse.Failed -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun gotoDateTimeStylist(
        str: String,
        service_provider: String,
        name: String,
        price: String
    ) {
        val providerNamelist = name.split(",")
        val mProviceName = providerNamelist
         Log.e("gotoDatePRoviderName-: ",providerNamelist.toString() )
        val services = requireArguments().getString("services_name", "").split(",")
        Log.e("gotoDateSErviceName-",services.toString())


       val providerServiceList = arrayListOf<DateProviderServiceModel>()
        providerNamelist.forEachIndexed { index, providerNameS ->

            providerServiceList.add(DateProviderServiceModel(services[index],providerNameS))

        }


     var listForShowingDataOnView  = ArrayList<DateProviderServiceModel>()
        listForShowingDataOnView = providerServiceList.distinctBy { it.providerName } as ArrayList<DateProviderServiceModel>
        Log.e( "gotoDateTimeStylist10554 ", listForShowingDataOnView.toString())


        val bundle = Bundle()
        bundle.putString("services", arguments?.getString("services", ""))
        bundle.putString("service_provider", service_provider)
        bundle.putString("duration", requireArguments().getString("duration", ""))
        bundle.putString("prices", price)
        bundle.putString("services_name", requireArguments().getString("services_name", ""))
        Log.e("gotoDateTimeStylist12085", name.toString())
        bundle.putString("provider_name", name)
        bundle.putString("durations", requireArguments().getString("durations", ""))
        bundle.putSerializable("showProviderService", listForShowingDataOnView)
        findNavController().navigate(R.id.dateAndTimeFragment, bundle)
    }

}