package besmart.elderlycare.model.user


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("cardID")
    val cardID: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("passwordHash")
    val passwordHash: String?,
    @SerializedName("staffID")
    val staffID: String?,
    @SerializedName("typeID")
    val typeID: Int?,
    @SerializedName("username")
    val username: String?
)