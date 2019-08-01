package besmart.elderlycare.model.sugar


import android.graphics.Color
import com.google.gson.annotations.SerializedName

data class SugarResponse(
    @SerializedName("cardID")
    val cardID: String?,
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("fbs")
    val fbs: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("staffID")
    val staffID: Int?
) {
    fun getResult(): String {
        return when {
            fbs!! >= 200 -> "เบาหวาน"
            fbs in 140..200 -> "เสี่ยงเป็นโรคเบาหวาน"
            else -> "ปกติ"
        }
    }

    fun getColor(): Int {
        return when {
            fbs!! >= 200 -> Color.parseColor("#FF4E00")
            fbs in 140..200 -> Color.parseColor("#FF9900")
            else -> Color.parseColor("#00C857")
        }
    }

    companion object {
        fun getResult(fbs: Int): String {
            return when {
                fbs >= 200 -> "เบาหวาน"
                fbs in 140..200 -> "เสี่ยงเป็นโรคเบาหวาน"
                else -> "ปกติ"
            }
        }
    }
}