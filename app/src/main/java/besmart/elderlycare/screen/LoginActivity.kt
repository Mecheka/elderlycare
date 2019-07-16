package besmart.elderlycare.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityLoginBinding
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
}
