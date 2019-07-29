package besmart.elderlycare.model.bodymass


import com.google.gson.annotations.SerializedName
import kotlin.math.pow

data class BodyMassResponce(
    @SerializedName("BMI")
    val bMI: Double?,
    @SerializedName("cardID")
    val cardID: String?,
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("staffID")
    val staffID: Int?,
    @SerializedName("weight")
    val weight: Int?
) {
    fun getWeigthResultByBMI(): String {
        return when {
            bMI!! >= 40 -> "โรคอ้วนขั้นรุนแรง"
            bMI >= 30 -> "โรคอ้วน"
            bMI >= 25 -> "น้ำหนักเกิน"
            bMI >= 18.5 -> "น้ำหนักปกติ"
            else -> "น้ำหนักน้อยกว่ามาตรฐาน"
        }
    }

    companion object{
        fun getWeigthResultByBMI(bMI:Float): String {
            return when {
                bMI >= 40 -> "โรคอ้วนขั้นรุนแรง"
                bMI >= 30 -> "โรคอ้วน"
                bMI >= 25 -> "น้ำหนักเกิน"
                bMI >= 18.5 -> "น้ำหนักปกติ"
                else -> "น้ำหนักน้อยกว่ามาตรฐาน"
            }
        }
        fun getBMI(weight: Float, height: Float): Float {
            if (weight == 0f || height == 0f) {
                return 0f
            }
            return weight / (height/100).pow(2)
        }
    }
}