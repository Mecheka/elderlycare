package besmart.elderlycare.model.login


import com.google.gson.annotations.SerializedName

data class LoginResponce(
    @SerializedName("expiresAt")
    val expiresAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("string")
    val string: String,
    @SerializedName("userID")
    val userID: Int,
    @SerializedName("userTypeID")
    val userTypeID: Int
)