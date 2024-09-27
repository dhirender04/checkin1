package com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.DateTimeUtils
import com.hubwallet.customer_checkin.databinding.LayoutEmployeeAvailableTimeBinding
import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.network.model.TimeAvailable
import java.util.*

class EmployeeTimeAvailabilityAdapter(
    val callback: (str: String, pos: Int) -> Unit, val duration: List<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var nameList = mutableListOf<String>()
    private var currentDate: String = ""
    private val stylistAppointment = mutableMapOf<String, Map<String, String>>()
    private val customerAppointment = mutableMapOf<String, Map<String, String>>()
    val map = mutableMapOf<String, List<String>>()
    val timeSelection = LinkedHashMap<Int, String>()
    private val timeAvailableDuration = LinkedHashMap<Int, String>()
    private val key = mutableListOf<String>()
    private var positionTobeUpdated = -1
    var timeSelected = ""
    var positionUpdated = -1
    var serviceName: List<String>? = null
    var context: Context? = null
    fun update(str: String, pos: Int) {

        var isSelectedPrevious = true
        for (i in 0 until pos) {
            if (timeSelection[i]?.isEmpty() == true) {
                isSelectedPrevious = false
                Toast.makeText(context, "Select ${key[i]} Time ", Toast.LENGTH_SHORT).show()
                return
            }
        }
        if (isSelectedPrevious) {
            val duration = duration[pos]
            timeSelected = str

            try {
                val time = Calendar.getInstance()
                time.time = DateTimeUtils.timeFormatter24.parse(str) as Date
                time.add(Calendar.MINUTE, duration.toInt())
                timeAvailableDuration[pos] = DateTimeUtils.timeFormatter24.format(time.time)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            positionUpdated = pos

            timeSelection[pos] = timeSelected
            notifyItemChanged(pos)
            if (pos < nameList.size) {
                for (i in pos until nameList.size-1) {
                    timeSelection[pos+1]=""
                    notifyItemChanged(pos + 1)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (context == null) context = parent.context
        val binding = DataBindingUtil.inflate<LayoutEmployeeAvailableTimeBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_employee_available_time,
            null,
            false
        )
        return EmployeeTimeAvailabilityViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, positions: Int) {


        val position = holder.absoluteAdapterPosition
        (holder as EmployeeTimeAvailabilityViewHolder).binding.rvAfterNoonSlot.layoutManager =
            GridLayoutManager(holder.itemView.context, 3)
        holder.binding.rvMorningSlot.layoutManager = GridLayoutManager(holder.itemView.context, 3)
        holder.binding.rvEveningSlot.layoutManager = GridLayoutManager(holder.itemView.context, 3)
        if (!timeSelection.containsKey(position)) {
            timeSelection.remove(position)
        }
        var list = map[key[position]]
        var timeDurationMin = Calendar.getInstance()
        var timeDurationMax = Calendar.getInstance()
        val map = mutableMapOf<String, List<TimeAvailable>>()

        list = list?.filter {
            val calendar = Calendar.getInstance().apply {
                time = DateTimeUtils.availableTimeFormatter.parse("$currentDate $it")
            }
            val timeNOw = Calendar.getInstance().timeInMillis
            calendar.timeInMillis > timeNOw

        }
        val morningList = list?.filter {
            it.substring(0, 2).toInt() < 12
        }?.map {
            val stylistTimes = stylistAppointment[key[position]]
            val isStylistAvailable = stylistTimes?.get(it) == "0"
            val customerAvailable = kotlin.run {
                var isav = true
                customerAppointment.keys.forEach { name ->
                    if (customerAppointment[name]?.get(it) == "1") {
                        isav = false
                    }
                }
                isav
            }
            var isSelected = false
            if (positionUpdated == positions) {
                isSelected = it == timeSelected
            }

            var isAlreadySelected = false
            var inDuration = false
            if (holder.absoluteAdapterPosition != 0) {
                isAlreadySelected = timeSelection[holder.absoluteAdapterPosition - 1] == it

                try {
                    timeDurationMin = Calendar.getInstance()
                    timeDurationMin.time =
                        DateTimeUtils.timeFormatter24.parse(timeSelection[holder.absoluteAdapterPosition - 1]) as Date
                    timeDurationMax.time =
                        DateTimeUtils.timeFormatter24.parse(timeAvailableDuration[holder.absoluteAdapterPosition - 1]) as Date
                    val ti = DateTimeUtils.timeFormatter24.parse(it)
                    val currentDate = Calendar.getInstance().apply { time = ti }

                    if (currentDate.timeInMillis > timeDurationMin.timeInMillis && currentDate.timeInMillis < timeDurationMax.timeInMillis) {
                        inDuration = true
                    }
                    if (timeSelection.containsKey(holder.absoluteAdapterPosition - 1)) {
                        val lastTime =
                            DateTimeUtils.timeFormatter24.parse(timeSelection[holder.absoluteAdapterPosition - 1]) as Date
                        val last = Calendar.getInstance().apply { time = lastTime }

                        if (currentDate.timeInMillis < last.timeInMillis) {
                            inDuration = true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            var isAvail =
                isStylistAvailable && customerAvailable && !isAlreadySelected && !inDuration


            TimeAvailable(
                it, isAvail, isSelected
            )
        }
        map["morning"] = morningList ?: emptyList()
        val afterNoonList = list?.filter {
            it.substring(0, 2).toInt() in 12..17
        }?.map {
            val stylistTimes = stylistAppointment[key[position]]
            val isStylistAvailable = stylistTimes?.get(it) == "0"
            val customerAvailable = kotlin.run {
                var isav = true
                customerAppointment.keys.forEach { name ->
                    if (customerAppointment[name]?.get(it) == "1") {
                        isav = false
                    }
                }
                isav
            }
            var isSelected = false
            if (positionUpdated == positions) {
                isSelected = it == timeSelected
            }

            var isAlreadySelected = false
            var inDuration = false
            if (holder.absoluteAdapterPosition != 0) {
                isAlreadySelected = timeSelection[holder.absoluteAdapterPosition - 1] == it

                try {
                    timeDurationMin = Calendar.getInstance()
                    timeDurationMin.time =
                        DateTimeUtils.timeFormatter24.parse(timeSelection[holder.absoluteAdapterPosition - 1]) as Date
                    timeDurationMax.time =
                        DateTimeUtils.timeFormatter24.parse(timeAvailableDuration[holder.absoluteAdapterPosition - 1]) as Date
                    val ti = DateTimeUtils.timeFormatter24.parse(it)
                    val currentDate = Calendar.getInstance().apply { time = ti }

                    if (currentDate.timeInMillis > timeDurationMin.timeInMillis && currentDate.timeInMillis < timeDurationMax.timeInMillis) {
                        inDuration = true
                    }
                    if (timeSelection.containsKey(holder.absoluteAdapterPosition - 1)) {
                        val lastTime =
                            DateTimeUtils.timeFormatter24.parse(timeSelection[holder.absoluteAdapterPosition - 1]) as Date
                        val last = Calendar.getInstance().apply { time = lastTime }

                        if (currentDate.timeInMillis < last.timeInMillis) {
                            inDuration = true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            var isAvail =
                isStylistAvailable && customerAvailable && !isAlreadySelected && !inDuration


            TimeAvailable(
                it, isAvail, isSelected
            )
        }

        map["afternoon"] = afterNoonList ?: emptyList()
        val eveningList = list?.filter {
            it.substring(0, 2).toInt() > 17
        }?.map {
            val stylistTimes = stylistAppointment[key[position]]
            val isStylistAvailable = stylistTimes?.get(it) == "0"
            val customerAvailable = kotlin.run {
                var isav = true
                customerAppointment.keys.forEach { name ->
                    if (customerAppointment[name]?.get(it) != "0") {
                        isav = false
                    }
                }
                isav
            }
            var isSelected = false
            if (positionUpdated == positions) {
                isSelected = it == timeSelected
            }

            var isAlreadySelected = false
            var inDuration = false
            if (holder.absoluteAdapterPosition != 0) {
                isAlreadySelected = timeSelection[holder.absoluteAdapterPosition - 1] == it
                try {
                    timeDurationMin = Calendar.getInstance()
                    timeDurationMin.time =
                        DateTimeUtils.timeFormatter24.parse(timeSelection[holder.absoluteAdapterPosition - 1]) as Date
                    timeDurationMax.time =
                        DateTimeUtils.timeFormatter24.parse(timeAvailableDuration[holder.absoluteAdapterPosition - 1]) as Date
                    val ti = DateTimeUtils.timeFormatter24.parse(it)
                    val currentDate = Calendar.getInstance().apply { time = ti }

                    if (currentDate.timeInMillis < timeDurationMax.timeInMillis) {
                        inDuration = true
                    }
                    if (timeSelection.containsKey(holder.absoluteAdapterPosition - 1)) {
                        val lastTime =
                            DateTimeUtils.timeFormatter24.parse(timeSelection[holder.absoluteAdapterPosition - 1]) as Date
                        val last = Calendar.getInstance().apply { time = lastTime }
                        Log.e("onBindlder:eve ", currentDate.timeInMillis.toString())
                        Log.e("onBinHolder:eve ", last.timeInMillis.toString())
                        if (currentDate.timeInMillis < last.timeInMillis) {
                            inDuration = true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            var isAvail =
                isStylistAvailable && customerAvailable && !isAlreadySelected && !inDuration

            TimeAvailable(
                it,
                isAvail,
                isSelected
            )
        }

        map["evening"] = eveningList ?: emptyList()

        val c = { str: String ->
            if (position != 0) {
                if (!timeSelection.containsKey(position - 1)) {
                    Toast.makeText(
                        holder.itemView.context,
                        "Select time for ${nameList[position - 1]}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    callback(str, position)
                    positionTobeUpdated = position
                }

            } else {
                callback(str, position)
                positionTobeUpdated = position

            }

        }

        val adapterEventing = DateAndTimeStylistAdapter(eveningList, c)
        val adapterAfterNoon = DateAndTimeStylistAdapter(afterNoonList, c)
        val adapterMorning = DateAndTimeStylistAdapter(morningList, c)
        holder.binding.rvEveningSlot.setVisibility(eveningList?.isEmpty() != true)
        holder.binding.rvAfterNoonSlot.setVisibility(afterNoonList?.isEmpty() != true)
        holder.binding.rvMorningSlot.setVisibility(morningList?.isEmpty() != true)
        holder.binding.notAvailable3.setVisibility(eveningList?.isEmpty() == true)
        holder.binding.notAvailable2.setVisibility(afterNoonList?.isEmpty() == true)
        holder.binding.notAvailable1.setVisibility(morningList?.isEmpty() == true)

        holder.binding.rvAfterNoonSlot.adapter = adapterAfterNoon
        holder.binding.rvEveningSlot.adapter = adapterEventing
        holder.binding.rvMorningSlot.adapter = adapterMorning
        Log.e("onBindPRoviderName", key.toString())
        Log.e("onBindServiceName", serviceName.toString())
        holder.binding.stylistName.text = "${key[position]} for ${serviceName?.get(position)}"


    }


    fun updateMap(
        map: Map<String, List<String>>,
        stylistAppointment: Map<String, Map<String, String>>,
        customerAppointment: Map<String, Map<String, String>>,
        currentDate: String,
        servicesName: List<String>,
        nameList: MutableList<String>
    ) {

        this.currentDate = currentDate
        this.map.clear()
        this.nameList.clear()
        this.nameList = nameList
        this.serviceName = servicesName
        this.stylistAppointment.clear()
        this.customerAppointment.clear()
        this.map.putAll(map)
        this.stylistAppointment.putAll(stylistAppointment)
        this.customerAppointment.putAll(customerAppointment)
        key.clear()
        timeSelected = ""
        timeSelection.clear()
        key.addAll(nameList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = nameList.size

}

class EmployeeTimeAvailabilityViewHolder(val binding: LayoutEmployeeAvailableTimeBinding) :
    RecyclerView.ViewHolder(binding.root)