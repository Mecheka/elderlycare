package besmart.elderlycare.model.blood


import android.graphics.Color
import com.google.gson.annotations.SerializedName

data class BloodPressuresResponse(
    @SerializedName("cardID")
    val cardID: String?,
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("dia")
    val dia: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("staffID")
    val staffID: Int?,
    @SerializedName("sys")
    val sys: Int?
) {
    fun getResult(): String {
        return if (sys!! < 120 && dia!! < 80) {
            "ความดันปกติ"
        } else if ((sys in 120..129) && (dia!! < 80)) {
            "กลุ่มเสี่ยงความดันสูง"
        } else if ((sys in 130..139) || (dia in 80..89)) {
            "ความดันสูงขั้นที่ 1"
        } else if ((sys in 140..179) || (dia in 90..119)) {
            "ความดันสูงขั้นที่ 2"
        } else if (sys >= 180 || dia!! >= 90) {
            "ความดันสูงขั้นวิกฤต"
        } else {
            "-"
        }
    }

    fun getResultColor(): Int {
        return if (sys!! < 120 && dia!! < 80) {
            Color.parseColor("#00C857")
        } else if ((sys in 120..129) && (dia!! < 80)) {
            Color.parseColor("#FFBE00")
        } else if ((sys in 130..139) || (dia in 80..89)) {
            Color.parseColor("#FF9900")
        } else if ((sys in 140..179) || (dia in 90..119)) {
            Color.parseColor("#FF4E00")
        } else if (sys >= 180 || dia!! >= 90) {
            Color.parseColor("#DE0101")
        } else {
            Color.parseColor("#00B1C8")
        }
    }

    companion object{
        fun getResult(sys:Float,dia:Float): String {
            return if (sys < 120 && dia < 80) {
                "ความดันปกติ"
            } else if ((sys in 120..129) && (dia < 80)) {
                "กลุ่มเสี่ยงความดันสูง"
            } else if ((sys in 130..139) || (dia in 80..89)) {
                "ความดันสูงขั้นที่ 1"
            } else if ((sys in 140..179) || (dia in 90..119)) {
                "ความดันสูงขั้นที่ 2"
            } else if (sys >= 180 || dia >= 90) {
                "ความดันสูงขั้นวิกฤต"
            } else {
                "-"
            }
        }

        fun getResultColor(sys: Float,dia: Float): Int {
            return if (sys < 120 && dia < 80) {
                Color.parseColor("#00C857")
            } else if ((sys in 120..129) && (dia < 80)) {
                Color.parseColor("#FFBE00")
            } else if ((sys in 130..139) || (dia in 80..89)) {
                Color.parseColor("#FF9900")
            } else if ((sys in 140..179) || (dia in 90..119)) {
                Color.parseColor("#FF4E00")
            } else if (sys >= 180 || dia >= 90) {
                Color.parseColor("#DE0101")
            } else {
                Color.parseColor("#00B1C8")
            }
        }
    }
}