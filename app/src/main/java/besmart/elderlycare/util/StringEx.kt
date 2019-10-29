package besmart.elderlycare.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.convertDate(): String {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    calendar.time = inputFormat.parse(this)!!
    val outputFormat = SimpleDateFormat(" dd MMM yyyy HH:mm", Locale("TH"))
    val output = outputFormat.format(calendar.time)
    return "วันที่ $output น."
}