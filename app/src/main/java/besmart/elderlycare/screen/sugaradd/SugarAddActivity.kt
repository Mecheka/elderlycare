package besmart.elderlycare.screen.sugaradd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivitySugarAddBinding
import kotlinx.android.synthetic.main.activity_main.*

class SugarAddActivity : AppCompatActivity() {

    companion object {
        const val PROFILE = "profile"
    }

    private lateinit var binding: ActivitySugarAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sugar_add)
        initInstance()
    }

    private fun initInstance(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}
