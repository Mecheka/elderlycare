package besmart.elderlycare.service

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.loadImageResourceCircle
import besmart.elderlycare.util.loadImageUrlCircle

@BindingAdapter("imageUrl", "error")
fun serImageUrl(view: ImageView, url: ObservableField<String>, res: Int) {
    url.get()?.let {
        view.loadImageUrlCircle(Constance.BASE_URL + "/" + it)
    }?:run{
        view.loadImageResourceCircle(res)
    }
}