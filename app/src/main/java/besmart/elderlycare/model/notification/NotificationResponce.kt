package besmart.elderlycare.model.notification


import com.google.gson.annotations.SerializedName

data class NotificationResponce(
    @SerializedName("cardID")
    val cardID: String,
    @SerializedName("createAt")
    val createAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("title")
    val title: String
)