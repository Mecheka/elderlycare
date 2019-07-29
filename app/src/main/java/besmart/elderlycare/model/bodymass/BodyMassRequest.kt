package besmart.elderlycare.model.bodymass

import com.google.gson.annotations.SerializedName

data class BodyMassRequest(
    @SerializedName("cardID")
    val cardID: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("height")
    val height: Float,
    @SerializedName("weight")
    val weight: Float
)