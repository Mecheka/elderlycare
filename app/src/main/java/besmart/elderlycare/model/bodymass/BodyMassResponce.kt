package besmart.elderlycare.model.bodymass


import com.google.gson.annotations.SerializedName

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
)