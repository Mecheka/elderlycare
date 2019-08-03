package besmart.elderlycare.screen.base

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import besmart.elderlycare.R

@SuppressLint("Registered")
open class BaseFragment : Fragment() {

    protected lateinit var dialog: AlertDialog

    fun showLoadingDialog(context: Context) {
        dialog = AlertDialog.Builder(context)
            .setCancelable(false)
            .setView(R.layout.dialog_loading).show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}