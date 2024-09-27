package com.hubwallet.customer_checkin.common.extensions

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun View.isNotValid(): Boolean {

    return when (this) {
        is TextView -> this.text.toString().isEmpty()
        is EditText -> this.text.toString().isEmpty()

        else -> false
    }
}

fun Float.toTwoDigit(): Float {
    val format = DecimalFormat("#.##")
    return format.format(this).toFloat()
}

fun Float.roundTwoDecimal(): Float {
    var rounded = BigDecimal.valueOf(getFloatAsDouble(this)).setScale(2, RoundingMode.HALF_UP)
    return rounded.toFloat()
}

fun Float.roundTwoDecimalNo(): String {
    val rounded = BigDecimal.valueOf(getFloatAsDouble(this)).setScale(2, RoundingMode.DOWN)
    return rounded.toString()
}
fun Float.roundTwoDecimal2(): Float {
    val rounded = BigDecimal.valueOf(getFloatAsDouble(this)).setScale(2, RoundingMode.DOWN)
    return rounded.toFloat()
}

fun Double.roundTwoDecimal(): Float {
    var rounded = BigDecimal.valueOf(this).setScale(2, RoundingMode.HALF_UP)
    return rounded.toFloat()
}

fun getFloatAsDouble(fValue: Float): Double {
    return java.lang.Double.valueOf(fValue.toString())
}


fun EditText.toCurrency(x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int,
        ) {


            if (s.toString().isNotEmpty() && s.toString() != current) {
                editText.removeTextChangedListener(this)
                if (s.toString().length == 1 && s.toString() == ".") {
                    editText.setText("")
                    editText.addTextChangedListener(this)
                    return
                }
                val cleanString: String = s.replace("""[$,.]""".toRegex(), "")

                val parsed = cleanString.toDouble()
                val formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100)).replace(",","")

                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)
                x(formatted)
                editText.addTextChangedListener(this)
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}

fun EditText.toCurrency_(x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int,
        ) {
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                editText.gravity = Gravity.END or Gravity.CENTER
                var cleanString: String = s.replace("""[$,%.]""".toRegex(), "")
                if (cleanString.isNullOrEmpty()) cleanString = "0"
                val parsed = cleanString.toDouble()
                var formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))
                // formatted  =formatted.replace("$","").replace(",","")
                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)
                x(formatted)
                editText.addTextChangedListener(this)
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}

fun EditText.toCurrency_dollar(x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int,
        ) {
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                editText.gravity = Gravity.END or Gravity.CENTER
                var cleanString: String = s.replace("""[$,.]""".toRegex(), "")
                if (cleanString.isEmpty()) cleanString = "0"
                val parsed = cleanString.toDouble()
                var formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))
                formatted = formatted.replace("$", "").replace(",", "")
                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)
                x(formatted)
                editText.addTextChangedListener(this)
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}

fun EditText.toCurrencyDollar(x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int,
        ) {
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                editText.gravity = Gravity.END or Gravity.CENTER
                var cleanString: String = s.replace("""[$,.]""".toRegex(), "")
                if (cleanString.isEmpty()) cleanString = "0"
                val parsed = cleanString.toDouble()
                var formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))
                formatted = formatted.replace(",", "")
                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)
                x(formatted)
                editText.addTextChangedListener(this)
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}

fun EditText.toCurrencyDollarWithLimit(float: Float, x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(

            s: CharSequence, start: Int, before: Int, count: Int,
        ) {
            Log.e("onTextChanged: ", s.toString() + "sad")
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                editText.gravity = Gravity.START or Gravity.CENTER
                var cleanString: String = s.toString().replace("""[$,.-]""".toRegex(), "")
                if (cleanString.isEmpty()) cleanString = "0"
                val parsed = cleanString.toDouble()

                if (parsed > (BigDecimal.valueOf((float * 100).toDouble())
                        .setScale(2, RoundingMode.HALF_UP).toFloat())
                ) {
                    if (current.isNotEmpty()) {
                        editText.setText("-$current")
                        editText.setSelection(current.length + 1)
                        x(current)
                    } else {
                        x("")
                        editText.setText("")
                    }
                } else {
                    var formatted =
                        NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))
                    formatted = formatted.replace(",", "")
                    current = formatted
                    if (formatted.isNotEmpty()) {
                        editText.setText("-$formatted")
                        x(formatted)
                        editText.setSelection(formatted.length + 1)
                    } else
                        editText.setText("")


                }
                editText.requestFocus()
                editText.addTextChangedListener(this)

            }

        }

        override fun afterTextChanged(p0: Editable?) {
            Log.e("onTextChanged: After", "Called")

        }
    }
    this.addTextChangedListener(textChangeListener)

}

fun EditText.toCurrencyDollarWithLimitAmount(float: Float, x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int,
        ) {
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                editText.gravity = Gravity.START or Gravity.CENTER
                var cleanString: String = s.toString().replace("""[$,.-]""".toRegex(), "")
                if (cleanString.isEmpty()) cleanString = "0"
                val parsed = cleanString.toDouble()

                if (parsed > (BigDecimal.valueOf((float * 100).toDouble())
                        .setScale(2, RoundingMode.HALF_UP).toFloat())
                ) {
                    if (current.isNotEmpty()) {
                        editText.setText("$current")
                        editText.setSelection(current.length)
                        x(current)
                    } else {
                        x("")
                        editText.setText("")
                    }
                } else {
                    var formatted =
                        NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))
                    formatted = formatted.replace(",", "")
                    current = formatted
                    if (formatted.isNotEmpty()) {
                        editText.setText("$formatted")
                        x(formatted)
                        editText.setSelection(formatted.length)
                    } else
                        editText.setText("")


                }
                editText.addTextChangedListener(this)

            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}

fun EditText.toCurrencyDollarWithLimitRefund(float: Float, x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int,
        ) {
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                editText.gravity = Gravity.START or Gravity.CENTER
                var cleanString: String = s.replace("""[$,.-]""".toRegex(), "")
                if (cleanString.isEmpty()) cleanString = "0"
                val parsed = cleanString.toDouble()
                Log.e("parsed: ", "${parsed.toInt()}")
                Log.e("parsed2: ", "${(float * 100).toInt()}")
                Log.e("parsed2: ", "${(float.toString().replace(".", "")).toInt()}")



                if (parsed.toBigDecimal() > (float.toBigDecimal().multiply(BigDecimal("100")))) {
                    if (current.isNotEmpty()) {
                        editText.setText("$current")
                        editText.setSelection(current.length)
                        x(current)
                    } else {
                        x("")
                        editText.setText("")
                    }
                } else {
                    var formatted =
                        NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))
                    formatted = formatted.replace(",", "")
                    current = formatted
                    if (formatted.isNotEmpty()) {
                        editText.setText("$formatted")
                        x(formatted)
                        editText.setSelection(formatted.length)
                    } else
                        editText.setText("")


                }
                editText.addTextChangedListener(this)

            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}

fun EditText.toCurrency_dollar_sign(x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int,
        ) {
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                editText.gravity = Gravity.START or Gravity.CENTER
                var cleanString: String = s.replace("""[$,.]""".toRegex(), "")
                if (cleanString.isNullOrEmpty()) cleanString = "0"
                val parsed = cleanString.toDouble()
                var formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))
                formatted = formatted.replace("$", "").replace(",", "")
                current = formatted
                editText.setText("$" + formatted)
                editText.setSelection(formatted.length)
                x(formatted)
                editText.addTextChangedListener(this)
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}

fun EditText.toPercentage(x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int,
        ) {


            if (s.toString() != current) {
                editText.removeTextChangedListener(this)

                var cleanString: String = s.replace("""[$,.]""".toRegex(), "")
                if (cleanString.isNullOrEmpty()) cleanString = "0"
                val parsed = (cleanString.toDouble())///100
                var formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))
                formatted = formatted.replace("$", "")
                if (formatted.toFloat() > 100.00) formatted = "100.00"
                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)
//                if(editText.text.toString().toFloat()>100.00){
//                    editText.error
//                    Toast.makeText(this,"Percentage should not be greater than ")
//                }else editText.error =null
                x(formatted)
                editText.addTextChangedListener(this)
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}

fun EditText.toPercentage2(x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int,
        ) {
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)

                var cleanString: String = s.replace("""[$,%.]""".toRegex(), "")
                // if(cleanString.toFloat()>100.00) cleanString ="100.00"
                val parsed = (cleanString.toDouble())///100
                var formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))
                formatted = formatted.replace("$", "")
                if (formatted.toFloat() > 100.00) formatted = "100.00"
                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)
//                if(editText.text.toString().toFloat()>100.00){
//                    editText.error
//                    Toast.makeText(this,"Percentage should not be greater than ")
//                }else editText.error =null

                x(formatted)
                editText.addTextChangedListener(this)
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}


fun EditText.toCurrencyWithNullCheck(x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int,
        ) {


            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                val cleanString: String = s.replace("""[$,.]""".toRegex(), "")
                if (cleanString.isNotEmpty()) {
                    val parsed = cleanString.toDouble()
                    val formatted =
                        NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))

                    current = formatted
                    editText.setText(formatted)
                    editText.setSelection(formatted.length)
                    x(formatted)
                }

                editText.addTextChangedListener(this)
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}

fun EditText.toPercentageWithNullCheck(x: (String) -> Unit) {
    val editText = this
    var current: String = ""
    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int,
        ) {
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                val cleanString: String = s.replace("""[$,%.]""".toRegex(), "")
                if (cleanString.isNotEmpty()) {
                    var parsed = cleanString.toDouble()
                    if (parsed > 10000) {
                        parsed = 10000.0
                    }
                    val df = DecimalFormat("0.00")
                    val formatted = df.format(parsed / 100) + "%"
                    current = formatted
                    editText.setText(formatted)
                    if (formatted.isNotEmpty()) editText.setSelection(formatted.length - 1)
                    x(formatted)

                }
                editText.addTextChangedListener(this)
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    this.addTextChangedListener(textChangeListener)

}
