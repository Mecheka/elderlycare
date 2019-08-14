package besmart.elderlycare.model.evaluation


import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("choices")
    val choices: List<Choice>?,
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("evaluationID")
    val evaluationID: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("questionNo")
    val questionNo: String?,
    @SerializedName("text")
    val text: String?
)