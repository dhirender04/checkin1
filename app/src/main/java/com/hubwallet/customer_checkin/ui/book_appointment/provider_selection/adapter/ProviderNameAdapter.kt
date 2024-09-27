package com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.adapter

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.databinding.LayoutProviderNameItemBinding
import com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.model.ProviderModel


class ProviderNameAdapter(val callbackStylistSelection: (String, String, String) -> Unit) :
    RecyclerView.Adapter<ProviderNameAdapter.ProviderNameViewModel>() {

    private var providerNameList = ArrayList<ProviderModel>()
    private val filterList = ArrayList<ProviderModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderNameViewModel {
        val binding = DataBindingUtil.inflate<LayoutProviderNameItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_provider_name_item, parent, false
        )
        return ProviderNameViewModel(binding)
    }

    override fun onBindViewHolder(holder: ProviderNameViewModel, position: Int) {
        val providerAdapter by lazy { ProviderAdapter() }
        holder.binding.apply {
            tvServiceTitle.text = filterList[position].serviceName
            rvProvider.adapter = providerAdapter
            providerAdapter.updateList(filterList[position].list)
        }
    }

    override fun getItemCount(): Int {
        return providerNameList.size
    }

    fun updateList(list: List<ProviderModel>) {
        //provider name and selected service name get
        providerNameList.clear()
        filterList.clear()
        providerNameList.addAll(list)
        filterList.addAll(list)
        notifyDataSetChanged()

    }

    fun getProviderData() {
        val lis = mutableListOf<String>()
        val name = mutableListOf<String>()
        val price = mutableListOf<String>()
        filterList.forEach { i ->
            i.list.forEach {
                if (it.isSelected) {
                    lis.add(it.providerId)
                    name.add(it.providerName)
                    it.price?.let { it1 -> price.add(it1) }
                }
            }
        }
        if (lis.size != filterList.size) {
            callbackStylistSelection("0", "", "")
        } else {
            Log.e("getProviderData: ", TextUtils.join(",", name) )
            callbackStylistSelection(
                lis.joinToString { it }.replace(" ", ""),
                TextUtils.join(",", name),
                price.joinToString { it }.replace(" ", "")
            )
        }
    }

    fun filterListBySearch(toString: String) {
        filterList.clear()
        if (toString.isEmpty()) {
            filterList.addAll(providerNameList)
        } else {
            providerNameList.forEach {
                val list2 = it.list.filter {
                    it.providerName.lowercase()
                        .contains(toString.lowercase()) || it.providerName.lowercase()
                        .contains("any")
                }
                filterList.add(ProviderModel(it.serviceName, list2))
            }
        }
        notifyDataSetChanged()
    }

    inner class ProviderNameViewModel(val binding: LayoutProviderNameItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}