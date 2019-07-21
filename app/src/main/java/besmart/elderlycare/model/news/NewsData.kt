package besmart.elderlycare.model.news

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsData(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("deleted_at")
    val deletedAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("news_contents")
    val newsContents: String?,
    @SerializedName("news_topic")
    val newsTopic: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
):Parcelable