package besmart.elderlycare.screen.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityLoginBinding
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.main.MainActivity
import kotlinx.android.synthetic.main.activity_select_user_type.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val selectType: String by lazy {
        intent.getStringExtra(SelectType.SELECTTYPE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getTitleByType()
        binding.userLayout.hint = getHintByType()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getTitleByType(): String {
        return when (selectType) {
            SelectType.ORSOMO -> getString(R.string.welcome_login) + " " + getString(R.string.orsomo)
            SelectType.HEALTH -> getString(R.string.welcome_login) + " " + getString(R.string.health)
            else -> getString(R.string.welcome_login) + " " + getString(R.string.person)
        }
    }

    private fun getHintByType():String{
        return when (selectType){
            SelectType.ORSOMO -> getString(R.string.orsomoId)
            SelectType.HEALTH -> getString(R.string.healthId)
            else -> getString(R.string.passportId)
        }
    }

    fun onLoginClick(view: View) {
        Intent().apply {
            this.setClass(this@LoginActivity, MainActivity::class.java)
            this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            this.putExtra(SelectType.SELECTTYPE, selectType)
            startActivity(this)
            this@LoginActivity.finish()
        }
    }
}
