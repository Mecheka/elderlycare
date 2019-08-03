package besmart.elderlycare.screen.splach

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import besmart.elderlycare.R
import besmart.elderlycare.model.login.LoginResponce
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.main.MainActivity
import besmart.elderlycare.screen.welcome.WelcomeActivity
import besmart.elderlycare.util.Constance
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_splach_screen.*

class SplachScreenActivity : AppCompatActivity() {

    private val profile = Hawk.get<ProfileResponce>(Constance.USER)
    private val auth = Hawk.get<LoginResponce>(Constance.TOKEN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach_screen)

        imageView.postDelayed( {
            if(profile == null && auth == null) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 1000)
    }
}
