package besmart.elderlycare.screen.splach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import besmart.elderlycare.R
import besmart.elderlycare.screen.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_splach_screen.*

class SplachScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach_screen)

        imageView.postDelayed( {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }, 1000)
    }
}
