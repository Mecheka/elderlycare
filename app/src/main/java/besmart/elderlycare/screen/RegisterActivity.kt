package besmart.elderlycare.screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityRegisterBinding
import besmart.elderlycare.screen.SelectType.Companion.HEALTH
import besmart.elderlycare.screen.SelectType.Companion.ORSOMO
import besmart.elderlycare.screen.SelectType.Companion.SELECTTYPE
import kotlinx.android.synthetic.main.activity_select_user_type.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val selectType: String by lazy {
        intent.getStringExtra(SELECTTYPE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getTitleByType()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        when (selectType) {
            ORSOMO -> {
                binding.orsomoLayout.visibility = View.VISIBLE
                binding.healthLayout.visibility = View.GONE
            }
            HEALTH -> {
                binding.orsomoLayout.visibility = View.GONE
                binding.healthLayout.visibility = View.VISIBLE
            }
            else -> {
                binding.orsomoLayout.visibility = View.GONE
                binding.healthLayout.visibility = View.GONE
            }
        }
    }

    private fun getTitleByType(): String {
        return when (selectType) {
            ORSOMO -> getString(R.string.register) + " " + getString(R.string.orsomo)
            HEALTH -> getString(R.string.register) + " " + getString(R.string.health)
            else -> getString(R.string.register) + " " + getString(R.string.person)
        }
    }
}
