package besmart.elderlycare.model.evaluation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EvaluationResponse(
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("text")
    val text: String?
):Parcelable