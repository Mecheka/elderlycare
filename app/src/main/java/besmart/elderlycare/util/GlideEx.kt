package besmart.elderlycare.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable
import besmart.elderlycare.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImageUrlCircle(
    url: String,
    errro: Drawable = R.drawable.baseline_person_24px.toDrawable()
) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .error(errro)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadImageResourceCircle(res: Int){
    Glide.with(this)
        .load(res)
        .apply(RequestOptions.circleCropTransform())
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(this)
}

fun ImageView.loadImageUrl(url:String){
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)
}

fun ImageView.loadImageResource(res: Int){
    Glide.with(this)
        .load(res)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)
}