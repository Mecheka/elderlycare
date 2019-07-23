package besmart.elderlycare.model.history


import com.google.gson.annotations.SerializedName

data class HistoryResponce(
    @SerializedName("cardID")
    val cardID: String?,
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("dataID")
    val dataID: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("staffID")
    val staffID: Int?,
    @SerializedName("typeID")
    val typeID: Int?
)