package besmart.elderlycare.model.profile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileResponce(
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("birthday")
    val birthday: String? = null,
    @SerializedName("cardID")
    val cardID: String? = null,
    @SerializedName("createAt")
    val createAt: String? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("genderID")
    val genderID: Int? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("imagePath")
    val imagePath: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
    @SerializedName("phone")
    val phone: String? = null
) : Parcelable