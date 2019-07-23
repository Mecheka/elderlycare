package besmart.elderlycare.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:imageUrl")
fun loadImage(view: ImageView, url: String) {
    view.loadImageUrlCircle(Constance.BASE_URL + "/" + url)
}