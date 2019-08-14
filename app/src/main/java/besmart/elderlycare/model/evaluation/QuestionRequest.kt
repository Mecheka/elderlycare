package besmart.elderlycare.model.evaluation

import com.google.gson.annotations.SerializedName

data class QuestionRequest(
    @SerializedName("cardID")
    val cardID:String,
    @SerializedName("evaluationID")
    val evaluationID:Int,
    @SerializedName("score")
    val score:Int,
    @SerializedName("json")
    val json:String
)