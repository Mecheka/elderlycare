package besmart.elderlycare

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import besmart.elderlycare.screen.SelectUserTypeActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    fun onRegisterClick(view: View) {
        Intent().apply {
            this.setClass(this@WelcomeActivity, SelectUserTypeActivity::class.java)
            this.putExtra(SelectUserTypeActivity.SELECT, SelectUserTypeActivity.REGISTER)
            startActivity(this)
        }
    }

    fun onLoginClick(view: View) {
        Intent().apply {
            this.setClass(this@WelcomeActivity, SelectUserTypeActivity::class.java)
            this.putExtra(SelectUserTypeActivity.SELECT, SelectUserTypeActivity.LOGIN)
            startActivity(this)
        }
    }
}
