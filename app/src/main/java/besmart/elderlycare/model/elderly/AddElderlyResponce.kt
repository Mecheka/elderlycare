package besmart.elderlycare.model.elderly


import com.google.gson.annotations.SerializedName

data class AddElderlyResponce(
    @SerializedName("createAt")
    val createAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("peopleCardID")
    val peopleCardID: String,
    @SerializedName("staffID")
    val staffID: Int
)