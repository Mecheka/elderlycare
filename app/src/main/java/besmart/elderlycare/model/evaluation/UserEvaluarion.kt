package besmart.elderlycare.model.evaluation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserEvaluarion(
    @SerializedName("cardID")
    val cardID: String?,
    @SerializedName("evaluationID")
    val evaluationID: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("json")
    val json: String?,
    @SerializedName("score")
    val score: Int?,
    @SerializedName("updateAt")
    val updateAt: String?,
    @SerializedName("createAt")
    val createAt:String
) : Parcelable