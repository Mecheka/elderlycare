package besmart.elderlycare.model.file


import com.google.gson.annotations.SerializedName

data class FileResponce(
    @SerializedName("data")
    val `data`: List<FileData>?,
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