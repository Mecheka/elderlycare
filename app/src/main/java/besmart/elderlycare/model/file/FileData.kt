package besmart.elderlycare.model.file


import com.google.gson.annotations.SerializedName

data class FileData(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("deleted_at")
    val deletedAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("path_file")
    val pathFile: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)