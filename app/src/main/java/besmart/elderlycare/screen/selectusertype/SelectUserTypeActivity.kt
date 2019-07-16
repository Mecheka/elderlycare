package besmart.elderlycare.screen.selectusertype

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import besmart.elderlycare.R
import besmart.elderlycare.screen.SelectType.Companion.HEALTH
import besmart.elderlycare.screen.SelectType.Companion.ORSOMO
import besmart.elderlycare.screen.SelectType.Companion.PERSON
import besmart.elderlycare.screen.SelectType.Companion.SELECTTYPE
import besmart.elderlycare.screen.login.LoginActivity
import besmart.elderlycare.screen.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_select_user_type.*

class SelectUserTypeActivity : AppCompatActivity() {

    companion object {
        const val LOGIN = "login"
        const val REGISTER = "register"
        const val SELECT = "select"
    }

    private val title: String by lazy {
        intent.getStringExtra(SELECT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user_type)
        setSupportActionBar(toolbar)
        supportActionBar?.title = if (title == LOGIN) {
            getString(R.string.welcome_login)
        } else {
            getString(R.string.register)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun onOrsomoClick(view: View) {
        navigateToAuth(ORSOMO)
    }

    fun onHealthClick(view: View) {
        navigateToAuth(HEALTH)
    }

    fun onPersonClick(view: View) {
        navigateToAuth(PERSON)
    }

    private fun navigateToAuth(type: String) {
        if (title == REGISTER) {
            Intent().apply {
                this.setClass(this@SelectUserTypeActivity, RegisterActivity::class.java)
                this.putExtra(SELECTTYPE, type)
                startActivity(this)
            }
        } else {
            Intent().apply {
                this.setClass(this@SelectUserTypeActivity, LoginActivity::class.java)
                this.putExtra(SELECTTYPE, type)
                startActivity(this)
            }
        }
    }
}
