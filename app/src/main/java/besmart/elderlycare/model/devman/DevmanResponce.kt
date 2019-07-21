package besmart.elderlycare.model.devman


import com.google.gson.annotations.SerializedName

data class DevmanResponce<T>(
    @SerializedName("data")
    val `data`: List<T>?,
    @SerializedName("current_page")
    val currentPage: Int?,
    @SerializedName("from")
    val from: Int?,
    @SerializedName("last_page")
    val lastPage: Int?,
    @SerializedName("next_page_url")
    val nextPageUrl: Any?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("per_page")
    val perPage: Int?,
    @SerializedName("prev_page_url")
    val prevPageUrl: Any?,
    @SerializedName("to")
    val to: Int?,
    @SerializedName("total")
    val total: Int?
)