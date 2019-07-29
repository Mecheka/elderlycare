package besmart.elderlycare.witget

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

import java.util.Calendar

import besmart.elderlycare.R

class MonthYearPickerDialog : DialogFragment() {
    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        // Get the layout inflater
        val inflater = activity!!.layoutInflater

        val cal = Calendar.getInstance()

        val dialog = inflater.inflate(R.layout.dialog_month_picker, null)
        val monthPicker = dialog.findViewById<View>(R.id.picker_month) as NumberPicker
        val yearPicker = dialog.findViewById<View>(R.id.picker_year) as NumberPicker

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = cal.get(Calendar.MONTH)+1

        val year = cal.get(Calendar.YEAR)
        yearPicker.minValue = year+543
        yearPicker.maxValue = MAX_YEAR
        yearPicker.value = year+543

        builder.setView(dialog)
            // Add action buttons
            .setPositiveButton("OK") { dialog, id ->
                listener!!.onDateSet(
                    null,
                    yearPicker.value,
                    monthPicker.value,
                    0
                )
            }
            .setNegativeButton("Cancel") { dialog, id -> this@MonthYearPickerDialog.dialog!!.cancel() }
        return builder.create()
    }

    companion object {

        private val MAX_YEAR = 2576
    }
}
