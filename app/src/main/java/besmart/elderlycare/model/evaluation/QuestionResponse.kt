package besmart.elderlycare.model.evaluation


import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("questions")
    val questions: List<Question>?,
    @SerializedName("text")
    val text: String?
)