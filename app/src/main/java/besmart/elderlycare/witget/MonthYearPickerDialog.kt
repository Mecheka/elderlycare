package besmart.elderlycare.witget

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import besmart.elderlycare.R
import java.util.*

class MonthYearPickerDialog : DialogFragment() {
    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener) {
        this.listener = listener
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater

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
            .setPositiveButton("OK") { _, _ ->
                listener!!.onDateSet(
                    null,
                    yearPicker.value,
                    monthPicker.value,
                    0
                )
            }
            .setNegativeButton("Cancel") { _, _ -> this@MonthYearPickerDialog.dialog?.cancel() }
        return builder.create()
    }

    companion object {

        private const val MAX_YEAR = 2576
    }
}
