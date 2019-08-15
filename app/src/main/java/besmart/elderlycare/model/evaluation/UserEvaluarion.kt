package besmart.elderlycare.model.evaluation


import com.google.gson.annotations.SerializedName

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
    val updateAt: String?
)