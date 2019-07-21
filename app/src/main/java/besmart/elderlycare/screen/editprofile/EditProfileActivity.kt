package besmart.elderlycare.screen.editprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import besmart.elderlycare.R

class EditProfileActivity : AppCompatActivity() {

    companion object{
        const val USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
    }
}
