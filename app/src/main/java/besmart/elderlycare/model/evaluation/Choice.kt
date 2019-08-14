package besmart.elderlycare.model.evaluation


import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("questionID")
    val questionID: Int?,
    @SerializedName("text")
    val text: String?
)