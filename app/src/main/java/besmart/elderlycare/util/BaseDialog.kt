package besmart.elderlycare.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

class BaseDialog {
    companion object {
        fun WarringDialog(context: Context, message: String) {
            AlertDialog.Builder(context)
                .setTitle("Warring")
                .setMessage(message)
                .setPositiveButton(
                    "OK"
                ) { dialog, which ->
                    dialog.dismiss()
                }.show()
        }
    }
}