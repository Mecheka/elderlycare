package besmart.elderlycare.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.convertDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val outputFormat = SimpleDateFormat(" dd MMM yyyy HH:mm", Locale("TH"))
    val input = inputFormat.parse(this)
    val output = outputFormat.format(input)
    return "วันที่ $output น."
}