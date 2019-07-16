package besmart.elderlycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
