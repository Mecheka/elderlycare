package besmart.elderlycare.model.login

import com.google.gson.annotations.SerializedName

data class FcmTokenRequest(
    @SerializedName("firebaseToken")
    val firebaseToken: String,
    @SerializedName("userID")
    val userID: Int,
    @SerializedName("platformID")
    val platformID: String = "2"
)