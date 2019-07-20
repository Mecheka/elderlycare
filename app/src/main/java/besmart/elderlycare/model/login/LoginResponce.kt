package besmart.elderlycare.model.login


import com.google.gson.annotations.SerializedName

data class LoginResponce(
    @SerializedName("expiresAt")
    val expiresAt: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("string")
    val string: String? = null,
    @SerializedName("userID")
    val userID: Int? = null,
    @SerializedName("userTypeID")
    val userTypeID: Int? = null
)