package besmart.elderlycare.model.profile


import com.google.gson.annotations.SerializedName

data class ProfileResponce(
    @SerializedName("address")
    val address: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("cardID")
    val cardID: String?,
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("genderID")
    val genderID: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("phone")
    val phone: String?
)