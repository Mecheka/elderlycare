package besmart.elderlycare.model.sugar


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
)