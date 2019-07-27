package besmart.elderlycare.model.blood


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
)