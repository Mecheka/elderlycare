package besmart.elderlycare.model.knowlege


import com.google.gson.annotations.SerializedName

data class KnowlegeResponce(
    @SerializedName("subdomain")
    val subdomain: String,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("topic")
    val topic: String
)