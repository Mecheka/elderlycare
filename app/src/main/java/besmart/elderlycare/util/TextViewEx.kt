package besmart.elderlycare.util

import android.widget.TextView
import androidx.annotation.StringRes

fun TextView.setStringRes(@StringRes res:Int){
    this.text = this.context.getString(res)
}