package besmart.elderlycare.model.editprofile

import com.google.gson.annotations.SerializedName

data class CreateProfileRequest(
    @SerializedName("cardID")
    val cardID: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("genderID")
    val genderID: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("phone")
    val phone: String
)