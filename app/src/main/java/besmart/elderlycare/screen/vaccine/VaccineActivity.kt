package besmart.elderlycare.screen.vaccine

import android.os.Bundle
import besmart.elderlycare.R
import besmart.elderlycare.screen.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class VaccineActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine)
        initInstance()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}
