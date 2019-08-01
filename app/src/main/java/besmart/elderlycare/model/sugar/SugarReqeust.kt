package besmart.elderlycare.model.sugar


import com.google.gson.annotations.SerializedName

data class SugarReqeust(
    @SerializedName("cardID")
    val cardID: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("fbs")
    val fbs: Int
)