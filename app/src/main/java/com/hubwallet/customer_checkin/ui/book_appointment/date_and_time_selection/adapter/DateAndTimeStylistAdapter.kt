package com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.utils.DateTimeUtils
import com.hubwallet.customer_checkin.databinding.LayoutAvailableTimeBinding
import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.network.model.TimeAvailable
import kotlinx.coroutines.*


class DateAndTimeStylistAdapter(
    val list: List<TimeAvailable>?,
    val callBack: (string: String) -> Unit
) :
    RecyclerView.Adapter<DateTimeViewHolderStylistViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DateTimeViewHolderStylistViewHolder {
        val binding = DataBindingUtil.inflate<LayoutAvailableTimeBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_available_time, null, false
        )
        return DateTimeViewHolderStylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateTimeViewHolderStylistViewHolder, position: Int) {
        try {

            val date =
                DateTimeUtils.timeFormatter24.parse(list?.get(holder.absoluteAdapterPosition)?.time)
            holder.binding.time.text = DateTimeUtils.timeFormatter12.format(date)
            if (list?.get(position)?.isAvailable == true) {
                if (list[position].isSelected) {
                    holder.binding.time.background = ContextCompat.getDrawable(
                        holder.binding.root.context,
                        R.drawable.time_booked
                    )
                    holder.binding.time.setTextColor(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.white
                        )
                    )
                } else {
                    holder.binding.time.background = ContextCompat.getDrawable(
                        holder.binding.root.context,
                        R.drawable.circular_grey_shape
                    )
                    holder.binding.time.setTextColor(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.black
                        )
                    )
                }

            } else {

                holder.binding.time.background = ContextCompat.getDrawable(
                    holder.binding.root.context,
                    R.drawable.time_unavailable
                )
                holder.binding.time.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.black
                    )
                )

            }

            holder.binding.time.setOnClickListener {
                if (list?.get(position)?.isAvailable == false) {
                    return@setOnClickListener
                }
                //  list?.get(position)?.isSelected = true
                // notifyItemChanged(position)

                    callBack(list?.get(position)?.time ?: "")


//                list?.forEachIndexed { i, n ->
//                    if (i != position) {
//                        n.isAvailable = true
//                    }
//                }
//                callBack(list?.get(position)?.time ?: "")
//                notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


}

class DateTimeViewHolderStylistViewHolder(val binding: LayoutAvailableTimeBinding) :
    RecyclerView.ViewHolder(binding.root)