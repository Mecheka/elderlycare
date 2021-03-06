package besmart.elderlycare.util

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog

class BaseDialog {
    companion object {
        fun warningDialog(context: Context, message: String) {

            MaterialDialog(context).show {
                title(text = "Warning")
                message(text = message)
                positiveButton(text = "OK")
            }
        }
    }
}