package besmart.elderlycare.model.blood


import com.google.gson.annotations.SerializedName

data class BloodPressuresRequest(
    @SerializedName("cardID")
    val cardID: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("dia")
    val dia: Float,
    @SerializedName("sys")
    val sys: Float
)