package besmart.elderlycare.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImageUrlCircle(url:String){
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun ImageView.loadImageResourceCircle(res: Int){
    Glide.with(this)
        .load(res)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun ImageView.loadImageUrl(url:String){
    Glide.with(this)
        .load(url)
        .centerCrop()
        .into(this)
}

fun ImageView.loadImageResource(res: Int){
    Glide.with(this)
        .load(res)
        .centerCrop()
        .into(this)
}