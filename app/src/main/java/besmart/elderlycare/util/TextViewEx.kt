package besmart.elderlycare.util

import android.widget.TextView

fun TextView.getStringByItem(res:Int){
    this.text = this.context.getString(res)
}